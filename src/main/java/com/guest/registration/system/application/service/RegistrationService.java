package com.guest.registration.system.application.service;

import com.guest.registration.system.application.dto.*;
import com.guest.registration.system.domain.model.ParticipantType;
import com.guest.registration.system.domain.validation.DomainValidationException;
import com.guest.registration.system.domain.validation.EstonianPersonalCodeValidator;
import com.guest.registration.system.infrastructure.entity.*;
import com.guest.registration.system.infrastructure.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationService {

    private final EventJpaRepository eventRepo;
    private final ParticipantJpaRepository participantRepo;
    private final PrivateParticipantJpaRepository privateRepo;
    private final CompanyParticipantJpaRepository companyRepo;
    private final RegistrationJpaRepository registrationRepo;
    private final PaymentMethodJpaRepository paymentMethodRepo;
    private final Clock clock;

    public RegistrationService(EventJpaRepository eventRepo,
                               ParticipantJpaRepository participantRepo,
                               PrivateParticipantJpaRepository privateRepo,
                               CompanyParticipantJpaRepository companyRepo,
                               RegistrationJpaRepository registrationRepo,
                               PaymentMethodJpaRepository paymentMethodRepo,
                               ParticipantService participantService,
                               Clock clock) {
        this.eventRepo = eventRepo;
        this.participantRepo = participantRepo;
        this.privateRepo = privateRepo;
        this.companyRepo = companyRepo;
        this.registrationRepo = registrationRepo;
        this.paymentMethodRepo = paymentMethodRepo;
        this.clock = clock;
    }

    @Transactional
    public Long addParticipantToEvent(Long eventId, AddParticipantToEventDTO dto) {

        EventEntity event = eventRepo.findById(eventId)
            .orElseThrow(() -> new DomainValidationException("Event not found: " + eventId));

        if (!event.getEventTime().isAfter(LocalDateTime.now(clock))) {
            throw new DomainValidationException("Cannot add participants to past event: " + eventId);
        }

        PaymentMethodEntity pm = paymentMethodRepo.findByCode(dto.getPaymentMethodCode())
            .orElseThrow(() -> new DomainValidationException("Unknown payment method: " + dto.getPaymentMethodCode()));

        // 1) Kui anti participantId, siis registreeri olemasolev
        Long participantId = dto.getParticipantId();
        if (participantId != null) {
            ParticipantEntity existing = participantRepo.findById(participantId)
                .orElseThrow(() -> new DomainValidationException("Participant not found: " + participantId));

            // ära lase sama eventi topelt
            if (registrationRepo.findByEvent_IdAndParticipant_Id(eventId, participantId).isPresent()) {
                throw new DomainValidationException("Participant already registered to this event");
            }

            RegistrationEntity reg = new RegistrationEntity();
            reg.setEvent(event);
            reg.setParticipant(existing);
            reg.setPaymentMethod(pm);
            reg.setAdditionalInfo(dto.getAdditionalInfo());

            return registrationRepo.save(reg).getId();
        }

        // 2) Muidu: "reuse-by-code" (või loo uus)
        ParticipantEntity participant = resolveOrCreateParticipant(dto);

        // ära lase sama eventi topelt
        if (registrationRepo.findByEvent_IdAndParticipant_Id(eventId, participant.getId()).isPresent()) {
            throw new DomainValidationException("Participant already registered to this event");
        }

        RegistrationEntity reg = new RegistrationEntity();
        reg.setEvent(event);
        reg.setParticipant(participant);
        reg.setPaymentMethod(pm);
        reg.setAdditionalInfo(dto.getAdditionalInfo());

        return registrationRepo.save(reg).getId();
    }

    @Transactional
    public List<EventParticipantListItemDTO> listEventParticipants(Long eventId) {
        if (!eventRepo.existsById(eventId)) {
            throw new DomainValidationException("Event not found: " + eventId);
        }

        List<RegistrationEntity> regs = registrationRepo.findByEvent_Id(eventId);

        return regs.stream().map(reg -> {
            ParticipantEntity p = reg.getParticipant();

            EventParticipantListItemDTO dto = new EventParticipantListItemDTO();
            dto.setParticipantId(p.getId());
            dto.setType(p.getType());

            if (p.getType() == ParticipantType.PRIVATE) {
                PrivateParticipantEntity details = privateRepo.findById(p.getId())
                    .orElseThrow(() -> new DomainValidationException("Private participant details not found: " + p.getId()));
                dto.setDisplayName(details.getFirstName() + " " + details.getLastName());
                dto.setIdentificationCode(details.getPersonalCode());
            } else {
                CompanyParticipantEntity details = companyRepo.findById(p.getId())
                    .orElseThrow(() -> new DomainValidationException("Company participant details not found: " + p.getId()));
                dto.setDisplayName(details.getLegalName());
                dto.setIdentificationCode(details.getRegistryCode());
            }

            return dto;
        }).toList();
    }

    @Transactional
    public void removeParticipantFromEvent(Long eventId, Long participantId) {
        
        if (!eventRepo.existsById(eventId)) {
            throw new DomainValidationException("Event not found: " + eventId);
        }

        registrationRepo.deleteByEvent_IdAndParticipant_Id(eventId, participantId);
    }

    private ParticipantEntity resolveOrCreateParticipant(AddParticipantToEventDTO dto) {

        ParticipantType type;
        try {
            type = ParticipantType.valueOf(dto.getParticipantType());
        } catch (Exception e) {
            throw new DomainValidationException("Invalid participantType: " + dto.getParticipantType());
        }

        if (type == ParticipantType.PRIVATE) {
            if (dto.getPersonalCode() == null || dto.getPersonalCode().isBlank()) {
                throw new DomainValidationException("personalCode is required for PRIVATE");
            }
            if (!EstonianPersonalCodeValidator.isValid(dto.getPersonalCode())) {
                throw new DomainValidationException("Invalid Estonian personal code");
            }

            // reuse existing by personalCode
            var existing = privateRepo.findByPersonalCode(dto.getPersonalCode());
            if (existing.isPresent()) {
                return existing.get().getParticipant();
            }

            // create new
            ParticipantEntity participant = new ParticipantEntity();
            participant.setType(ParticipantType.PRIVATE);
            participantRepo.save(participant);

            PrivateParticipantEntity details = new PrivateParticipantEntity();
            details.setParticipant(participant);
            details.setFirstName(dto.getFirstName());
            details.setLastName(dto.getLastName());
            details.setPersonalCode(dto.getPersonalCode());
            details.setAdditionalInfo(dto.getAdditionalInfo());

            privateRepo.save(details);
            return participant;
        }

        if (type == ParticipantType.COMPANY) {
            if (dto.getRegistryCode() == null || dto.getRegistryCode().isBlank()) {
                throw new DomainValidationException("registryCode is required for COMPANY");
            }
            if (dto.getAttendeeCount() == null || dto.getAttendeeCount() <= 0) {
                throw new DomainValidationException("attendeeCount must be > 0");
            }

            // reuse existing by registryCode
            var existing = companyRepo.findByRegistryCode(dto.getRegistryCode());
            if (existing.isPresent()) {
                return existing.get().getParticipant();
            }

            // create new
            ParticipantEntity participant = new ParticipantEntity();
            participant.setType(ParticipantType.COMPANY);
            participantRepo.save(participant);

            CompanyParticipantEntity details = new CompanyParticipantEntity();
            details.setParticipant(participant);
            details.setLegalName(dto.getLegalName());
            details.setRegistryCode(dto.getRegistryCode());
            details.setAttendeeCount(dto.getAttendeeCount());
            details.setAdditionalInfo(dto.getAdditionalInfo());

            companyRepo.save(details);
            return participant;
        }

        throw new DomainValidationException("Unknown participantType: " + dto.getParticipantType());
    }

}

