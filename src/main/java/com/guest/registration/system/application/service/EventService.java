package com.guest.registration.system.application.service;

import com.guest.registration.system.application.dto.EventDTO;
import com.guest.registration.system.application.mapper.EventDTOMapper;
import com.guest.registration.system.infrastructure.entity.EventEntity;
import com.guest.registration.system.infrastructure.repository.EventJpaRepository;
import com.guest.registration.system.infrastructure.repository.RegistrationJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class EventService {

  private final EventJpaRepository eventRepo;
  private final RegistrationJpaRepository registrationRepo;
  private final Clock clock;

  public EventService(EventJpaRepository eventRepo,
                      RegistrationJpaRepository registrationRepo,
                      Clock clock) {
    this.eventRepo = eventRepo;
    this.registrationRepo = registrationRepo;
    this.clock = clock;
  }

  @Transactional
  public EventDTO getEvent(Long eventId) {
    EventEntity event = eventRepo.findById(eventId)
        .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));

    long count = registrationRepo.countByEvent_Id(eventId);
    return EventDTOMapper.toDto(event, count, clock);
  }


  @Transactional
  public Long createEvent(EventDTO dto) {
    if (dto.getEventDateTime() == null) {
      throw new IllegalArgumentException("Event time is required");
    }

    LocalDateTime now = LocalDateTime.now(clock);
    if (!dto.getEventDateTime().isAfter(now)) {
      throw new IllegalArgumentException("Event must be in the future");
    }

    EventEntity saved = eventRepo.save(EventDTOMapper.toEntity(dto));
    return saved.getId();
  }

  @Transactional
  public List<EventDTO> listEvents() {
    List<EventEntity> events = eventRepo.findAll();
    events.sort(Comparator.comparing(EventEntity::getEventTime));

    return events.stream()
        .map(event -> {
          long count = registrationRepo.countByEvent_Id(event.getId());
          return EventDTOMapper.toDto(event, count, clock);
        })
        .toList();
  }

  @Transactional
  public void deleteFutureEvent(Long eventId) {
    EventEntity event = eventRepo.findById(eventId)
        .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));

    LocalDateTime now = LocalDateTime.now(clock);
    if (!event.getEventTime().isAfter(now)) {
      throw new IllegalArgumentException("Only future events can be deleted");
    }

    eventRepo.delete(event);
  }

  @Transactional
  public void updateFutureEvent(Long eventId, EventDTO dto) {
    EventEntity event = eventRepo.findById(eventId)
        .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));

    LocalDateTime now = LocalDateTime.now(clock);
    if (!event.getEventTime().isAfter(now)) {
      throw new IllegalArgumentException("Only future events can be updated");
    }

    if (dto.getEventDateTime() == null) {
      throw new IllegalArgumentException("Event time is required");
    }
    if (!dto.getEventDateTime().isAfter(now)) {
      throw new IllegalArgumentException("Event must be in the future");
    }

    event.setName(dto.getName());
    event.setEventTime(dto.getEventDateTime());
    event.setLocation(dto.getLocation());
    event.setAdditionalInfo(dto.getAdditionalInfo());

    eventRepo.save(event);
  }

}
