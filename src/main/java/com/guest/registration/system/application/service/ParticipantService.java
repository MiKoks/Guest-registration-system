package com.guest.registration.system.application.service;

import com.guest.registration.system.application.dto.CompanyParticipantDTO;
import com.guest.registration.system.application.dto.ParticipantDetailsDTO;
import com.guest.registration.system.application.dto.PrivateParticipantDTO;
import com.guest.registration.system.domain.model.ParticipantType;
import com.guest.registration.system.domain.validation.DomainValidationException;
import com.guest.registration.system.domain.validation.EstonianPersonalCodeValidator;
import com.guest.registration.system.infrastructure.entity.CompanyParticipantEntity;
import com.guest.registration.system.infrastructure.entity.ParticipantEntity;
import com.guest.registration.system.infrastructure.entity.PrivateParticipantEntity;
import com.guest.registration.system.infrastructure.repository.CompanyParticipantJpaRepository;
import com.guest.registration.system.infrastructure.repository.ParticipantJpaRepository;
import com.guest.registration.system.infrastructure.repository.PrivateParticipantJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ParticipantService {

    private final ParticipantJpaRepository participantRepo;
    private final PrivateParticipantJpaRepository privateRepo;
    private final CompanyParticipantJpaRepository companyRepo;

    public ParticipantService(ParticipantJpaRepository participantRepo,
                              PrivateParticipantJpaRepository privateRepo,
                              CompanyParticipantJpaRepository companyRepo) {
        this.participantRepo = participantRepo;
        this.privateRepo = privateRepo;
        this.companyRepo = companyRepo;
    }

    @Transactional
    public Long createPrivateParticipant(PrivateParticipantDTO dto) {

        if (!EstonianPersonalCodeValidator.isValid(dto.getPersonalCode())) {
            throw new DomainValidationException("Invalid Estonian personal code");
        }

        ParticipantEntity participant = new ParticipantEntity();
        participant.setType(ParticipantType.PRIVATE);
        participantRepo.save(participant);

        PrivateParticipantEntity privateEntity = new PrivateParticipantEntity();
        privateEntity.setParticipant(participant);
        privateEntity.setFirstName(dto.getFirstName());
        privateEntity.setLastName(dto.getLastName());
        privateEntity.setPersonalCode(dto.getPersonalCode());
        privateEntity.setAdditionalInfo(dto.getAdditionalInfo());

        privateRepo.save(privateEntity);

        return participant.getId();
    }

    @Transactional
    public void updatePrivateParticipant(Long participantId, PrivateParticipantDTO dto) {
        ParticipantEntity participant = participantRepo.findById(participantId)
            .orElseThrow(() -> new DomainValidationException("Participant not found: " + participantId));

        if (participant.getType() != ParticipantType.PRIVATE) {
            throw new DomainValidationException("Participant is not PRIVATE: " + participantId);
        }

        if (!EstonianPersonalCodeValidator.isValid(dto.getPersonalCode())) {
            throw new DomainValidationException("Invalid Estonian personal code");
        }

        PrivateParticipantEntity details = privateRepo.findById(participantId)
            .orElseThrow(() -> new DomainValidationException("Private participant details not found: " + participantId));

        details.setFirstName(dto.getFirstName());
        details.setLastName(dto.getLastName());
        details.setPersonalCode(dto.getPersonalCode());
        details.setAdditionalInfo(dto.getAdditionalInfo());

        privateRepo.save(details);
    }

    @Transactional
    public Long createCompanyParticipant(CompanyParticipantDTO dto) {

        if (dto.getAttendeeCount() <= 0) {
            throw new DomainValidationException("Attendee count must be greater than zero");
        }

        ParticipantEntity participant = new ParticipantEntity();
        participant.setType(ParticipantType.COMPANY);
        participantRepo.save(participant);

        CompanyParticipantEntity companyEntity = new CompanyParticipantEntity();
        companyEntity.setParticipant(participant);
        companyEntity.setLegalName(dto.getLegalName());
        companyEntity.setRegistryCode(dto.getRegistryCode());
        companyEntity.setAttendeeCount(dto.getAttendeeCount());
        companyEntity.setAdditionalInfo(dto.getAdditionalInfo());

        companyRepo.save(companyEntity);

        return participant.getId();
    }

    @Transactional
    public void updateCompanyParticipant(Long participantId, CompanyParticipantDTO dto) {
        ParticipantEntity participant = participantRepo.findById(participantId)
            .orElseThrow(() -> new DomainValidationException("Participant not found: " + participantId));

        if (participant.getType() != ParticipantType.COMPANY) {
            throw new DomainValidationException("Participant is not COMPANY: " + participantId);
        }

        if (dto.getAttendeeCount() <= 0) {
            throw new DomainValidationException("Attendee count must be greater than zero");
        }

        CompanyParticipantEntity details = companyRepo.findById(participantId)
            .orElseThrow(() -> new DomainValidationException("Company participant details not found: " + participantId));

        details.setLegalName(dto.getLegalName());
        details.setRegistryCode(dto.getRegistryCode());
        details.setAttendeeCount(dto.getAttendeeCount());
        details.setAdditionalInfo(dto.getAdditionalInfo());

        companyRepo.save(details);
    }

    @Transactional
    public ParticipantDetailsDTO getParticipantDetails(Long participantId) {

        ParticipantEntity participant = participantRepo.findById(participantId)
            .orElseThrow(() -> new DomainValidationException("Participant not found: " + participantId));

        ParticipantDetailsDTO dto = new ParticipantDetailsDTO();
        dto.setId(participant.getId());
        dto.setType(participant.getType());

        if (participant.getType() == ParticipantType.PRIVATE) {
            PrivateParticipantEntity details = privateRepo.findById(participantId)
                .orElseThrow(() -> new DomainValidationException("Private participant details not found: " + participantId));

            dto.setFirstName(details.getFirstName());
            dto.setLastName(details.getLastName());
            dto.setPersonalCode(details.getPersonalCode());
            dto.setPrivateAdditionalInfo(details.getAdditionalInfo());
            return dto;
        }

        if (participant.getType() == ParticipantType.COMPANY) {
            CompanyParticipantEntity details = companyRepo.findById(participantId)
                .orElseThrow(() -> new DomainValidationException("Company participant details not found: " + participantId));

            dto.setLegalName(details.getLegalName());
            dto.setRegistryCode(details.getRegistryCode());
            dto.setAttendeeCount(details.getAttendeeCount());
            dto.setCompanyAdditionalInfo(details.getAdditionalInfo());
            return dto;
        }

        throw new DomainValidationException("Unknown participant type: " + participant.getType());
    }
}

