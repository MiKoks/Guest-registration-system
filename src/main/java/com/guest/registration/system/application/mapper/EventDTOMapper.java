package com.guest.registration.system.application.mapper;

import com.guest.registration.system.application.dto.EventDTO;
import com.guest.registration.system.infrastructure.entity.EventEntity;

import java.time.Clock;
import java.time.LocalDateTime;

public final class EventDTOMapper {

  private EventDTOMapper() {}

  // DTO -> Entity (Create/Update puhul)
  public static EventEntity toEntity(EventDTO dto) {
    EventEntity entity = new EventEntity();
    entity.setId(dto.getId());
    entity.setName(dto.getName());
    entity.setEventTime(dto.getEventDateTime());
    entity.setLocation(dto.getLocation());
    entity.setAdditionalInfo(dto.getAdditionalInfo());
    return entity;
  }

  // Entity -> DTO (List/Detail puhul)
  public static EventDTO toDto(EventEntity entity, long participantCount, Clock clock) {
    EventDTO dto = new EventDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setEventDateTime(entity.getEventTime()); // NB: entity field soovituslikult eventTime
    dto.setLocation(entity.getLocation());
    dto.setAdditionalInfo(entity.getAdditionalInfo());

    dto.setParticipantCount((int) participantCount);

    LocalDateTime now = LocalDateTime.now(clock);
    dto.setFutureEvent(entity.getEventTime().isAfter(now));

    return dto;
  }
}
