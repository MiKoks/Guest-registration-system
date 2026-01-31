package com.guest.registration.system;

import com.guest.registration.system.application.dto.AddParticipantToEventDTO;
import com.guest.registration.system.application.dto.EventParticipantListItemDTO;
import com.guest.registration.system.application.service.RegistrationService;
import com.guest.registration.system.domain.validation.DomainValidationException;
import com.guest.registration.system.infrastructure.entity.EventEntity;
import com.guest.registration.system.infrastructure.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class RegistrationServiceTest {

    @Autowired private RegistrationService registrationService;
    @Autowired private EventJpaRepository eventRepo;
    @Autowired private RegistrationJpaRepository registrationRepo;
    @Autowired private PrivateParticipantJpaRepository privateRepo;
    @Autowired private CompanyParticipantJpaRepository companyRepo;
    @Autowired private ParticipantJpaRepository participantRepo;
    @Autowired private Clock clock;

    @BeforeEach
    void setup() {
        registrationRepo.deleteAllInBatch();
        privateRepo.deleteAllInBatch();
        companyRepo.deleteAllInBatch();
        participantRepo.deleteAllInBatch();
        eventRepo.deleteAllInBatch();
    }

    @Test
    void addParticipantToFutureEvent_thenListAndRemove_shouldWork() {
        Long eventId = createEvent(LocalDateTime.now(clock).plusDays(2));

        AddParticipantToEventDTO dto = new AddParticipantToEventDTO();
        dto.setParticipantType("PRIVATE");
        dto.setFirstName("Näide");
        dto.setLastName("Tamm");
        dto.setPersonalCode("37605030299");
        dto.setPaymentMethodCode("CASH");
        dto.setAdditionalInfo("Soovid...");

        Long registrationId = registrationService.addParticipantToEvent(eventId, dto);
        assertThat(registrationId).isNotNull();

        List<EventParticipantListItemDTO> list = registrationService.listEventParticipants(eventId);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getDisplayName()).contains("Näide");
        assertThat(list.get(0).getIdentificationCode()).isNotBlank();

        Long participantId = list.get(0).getParticipantId();
        registrationService.removeParticipantFromEvent(eventId, participantId);

        List<EventParticipantListItemDTO> after = registrationService.listEventParticipants(eventId);
        assertThat(after).isEmpty();
    }

    @Test
    void addParticipantToPastEvent_shouldFail() {
        Long eventId = createEvent(LocalDateTime.now(clock).minusDays(1));

        AddParticipantToEventDTO dto = new AddParticipantToEventDTO();
        dto.setParticipantType("COMPANY");
        dto.setLegalName("Näide OÜ");
        dto.setRegistryCode("12345678");
        dto.setAttendeeCount(2);
        dto.setPaymentMethodCode("CASH");

        assertThatThrownBy(() -> registrationService.addParticipantToEvent(eventId, dto))
            .isInstanceOf(DomainValidationException.class)
            .hasMessageContaining("past");
    }

    @Test
    void addParticipant_invalidPaymentMethod_shouldFail() {
        Long eventId = createEvent(LocalDateTime.now(clock).plusDays(1));

        AddParticipantToEventDTO dto = new AddParticipantToEventDTO();
        dto.setParticipantType("COMPANY");
        dto.setLegalName("Näide OÜ");
        dto.setRegistryCode("12345678");
        dto.setAttendeeCount(2);
        dto.setPaymentMethodCode("DOES_NOT_EXIST");

        assertThatThrownBy(() -> registrationService.addParticipantToEvent(eventId, dto))
            .isInstanceOf(DomainValidationException.class)
            .hasMessageContaining("payment");
    }

    @Test
    void samePrivateParticipant_canBeAddedToMultipleEvents() {
        Long event1 = createEvent(LocalDateTime.now(clock).plusDays(2));
        Long event2 = createEvent(LocalDateTime.now(clock).plusDays(3));

        AddParticipantToEventDTO dto1 = new AddParticipantToEventDTO();
        dto1.setParticipantType("PRIVATE");
        dto1.setFirstName("Näide");
        dto1.setLastName("Tamm");
        dto1.setPersonalCode("37605030299");
        dto1.setPaymentMethodCode("CASH");
        dto1.setAdditionalInfo("Event 1");

        registrationService.addParticipantToEvent(event1, dto1);

        AddParticipantToEventDTO dto2 = new AddParticipantToEventDTO();
        dto2.setParticipantType("PRIVATE");
        dto2.setFirstName("Näide");
        dto2.setLastName("Tamm");
        dto2.setPersonalCode("37605030299");
        dto2.setPaymentMethodCode("CASH");
        dto2.setAdditionalInfo("Event 2");

        registrationService.addParticipantToEvent(event2, dto2);

        assertThat(registrationRepo.countByEvent_Id(event1)).isEqualTo(1);
        assertThat(registrationRepo.countByEvent_Id(event2)).isEqualTo(1);

        assertThat(participantRepo.count()).isEqualTo(1);
        assertThat(registrationRepo.count()).isEqualTo(2);
    }

    @Test
    void sameParticipant_cannotBeAddedTwiceToSameEvent_shouldFail() {
        Long eventId = createEvent(LocalDateTime.now(clock).plusDays(2));

        AddParticipantToEventDTO dto = new AddParticipantToEventDTO();
        dto.setParticipantType("PRIVATE");
        dto.setFirstName("Näide");
        dto.setLastName("Tamm");
        dto.setPersonalCode("37605030299");
        dto.setPaymentMethodCode("CASH");

        registrationService.addParticipantToEvent(eventId, dto);

        assertThatThrownBy(() -> registrationService.addParticipantToEvent(eventId, dto))
            .isInstanceOfAny(DomainValidationException.class, org.springframework.dao.DataIntegrityViolationException.class);

        assertThat(registrationRepo.countByEvent_Id(eventId)).isEqualTo(1);
        assertThat(participantRepo.count()).isEqualTo(1);
    }

    private Long createEvent(LocalDateTime time) {
        EventEntity e = new EventEntity();
        e.setName("Test Event");
        e.setEventTime(time);
        e.setLocation("Tallinn");
        e.setAdditionalInfo("x");
        return eventRepo.save(e).getId();
    }
}

