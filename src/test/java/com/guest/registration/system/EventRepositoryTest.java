package com.guest.registration.system;

import com.guest.registration.system.infrastructure.entity.EventEntity;
import com.guest.registration.system.infrastructure.repository.EventJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EventRepositoryTest {
    @Autowired
    private EventJpaRepository eventRepo;

    @Test
    void SaveEvent() {
        EventEntity event = new EventEntity();
        event.setName("Test Event");
        event.setEventTime(LocalDateTime.now().plusDays(1));
        event.setLocation("Tallinn");
        event.setAdditionalInfo("Hello");

        EventEntity saved = eventRepo.save(event);

        assertThat(saved.getId()).isNotNull();
        assertThat(eventRepo.findById(saved.getId())).isPresent();
    }
}
