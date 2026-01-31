package com.guest.registration.system.presentation.controllers;

import com.guest.registration.system.application.dto.AddParticipantToEventDTO;
import com.guest.registration.system.application.dto.EventParticipantListItemDTO;
import com.guest.registration.system.application.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/events/{eventId}")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/participants")
    public List<EventParticipantListItemDTO> listParticipants(@PathVariable Long eventId) {
        return registrationService.listEventParticipants(eventId);
    }

    @PostMapping("/participants")
    public ResponseEntity<Void> addParticipant(@PathVariable Long eventId,
                                               @Valid @RequestBody AddParticipantToEventDTO dto) {
        Long registrationId = registrationService.addParticipantToEvent(eventId, dto);
        return ResponseEntity.created(URI.create("/api/events/" + eventId + "/participants?registrationId=" + registrationId)).build();
    }

    @DeleteMapping("/participants/{participantId}")
    public ResponseEntity<Void> removeParticipant(@PathVariable Long eventId,
                                                  @PathVariable Long participantId) {
        registrationService.removeParticipantFromEvent(eventId, participantId);
        return ResponseEntity.noContent().build();
    }
}
