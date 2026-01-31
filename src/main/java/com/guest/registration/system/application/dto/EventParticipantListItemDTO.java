package com.guest.registration.system.application.dto;

import com.guest.registration.system.domain.model.ParticipantType;

public class EventParticipantListItemDTO {

    private Long participantId;
    private ParticipantType type;

    private String displayName;
    private String identificationCode;

    public Long getParticipantId() { return participantId; }
    public void setParticipantId(Long participantId) { this.participantId = participantId; }

    public ParticipantType getType() { return type; }
    public void setType(ParticipantType type) { this.type = type; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getIdentificationCode() { return identificationCode; }
    public void setIdentificationCode(String identificationCode) { this.identificationCode = identificationCode; }
}
