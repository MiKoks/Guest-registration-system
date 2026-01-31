package com.guest.registration.system.presentation.controllers;

import com.guest.registration.system.application.dto.CompanyParticipantDTO;
import com.guest.registration.system.application.dto.ParticipantDetailsDTO;
import com.guest.registration.system.application.dto.PrivateParticipantDTO;
import com.guest.registration.system.application.service.ParticipantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping("/{id}")
    public ParticipantDetailsDTO getDetails(@PathVariable Long id) {
        return participantService.getParticipantDetails(id);
    }

    @PostMapping("/private")
    public ResponseEntity<Void> createPrivate(@Valid @RequestBody PrivateParticipantDTO dto) {
        Long id = participantService.createPrivateParticipant(dto);
        return ResponseEntity.created(URI.create("/api/participants/" + id)).build();
    }

    @PutMapping("/{id}/private")
    public ResponseEntity<Void> updatePrivate(@PathVariable Long id,
                                              @Valid @RequestBody PrivateParticipantDTO dto) {
        participantService.updatePrivateParticipant(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/company")
    public ResponseEntity<Void> createCompany(@Valid @RequestBody CompanyParticipantDTO dto) {
        Long id = participantService.createCompanyParticipant(dto);
        return ResponseEntity.created(URI.create("/api/participants/" + id)).build();
    }

        @PutMapping("/{id}/company")
    public ResponseEntity<Void> updateCompany(@PathVariable Long id,
                                              @Valid @RequestBody CompanyParticipantDTO dto) {
        participantService.updateCompanyParticipant(id, dto);
        return ResponseEntity.noContent().build();
    }
}

