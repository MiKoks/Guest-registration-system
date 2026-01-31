package com.guest.registration.system.presentation.controllers;

import com.guest.registration.system.application.dto.EventDTO;
import com.guest.registration.system.application.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

  private final EventService eventService;

  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  // Avalehe: eventide list
  @GetMapping
  public List<EventDTO> listEvents() {
    return eventService.listEvents();
  }

  @GetMapping("/{id}")
  public EventDTO getEvent(@PathVariable Long id) {
    return eventService.getEvent(id);
  }

  // Eventi lisamine
  @PostMapping
  public ResponseEntity<Void> createEvent(@Valid @RequestBody EventDTO dto) {
    Long id = eventService.createEvent(dto);
    return ResponseEntity
        .created(URI.create("/api/events/" + id))
        .build();
  }

  // Eventi uuendamine
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDTO dto) {
    eventService.updateFutureEvent(id, dto);
    return ResponseEntity.noContent().build();
  }


  // Eventi kustutamine (ainult tulevikus)
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
    eventService.deleteFutureEvent(id);
    return ResponseEntity.noContent().build();
  }
}
