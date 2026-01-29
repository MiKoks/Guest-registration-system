package com.guest.registration.system.infrastructure.entity;

import jakarta.persistence.*;

/**
 * JPA entity for company participant details (table: company_participants).
 * Uses shared primary key with ParticipantEntity (participant_id is both PK and FK).
 */
@Entity
@Table(name = "company_participants")
public class CompanyParticipantEntity {

    @Id
    @Column(name = "participant_id")
    private Long participantId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "participant_id")
    private ParticipantEntity participant;

    @Column(name = "legal_name", nullable = false, length = 255)
    private String legalName;

    @Column(name = "registry_code", nullable = false, length = 50, unique = true)
    private String registryCode;

    @Column(name = "attendee_count", nullable = false)
    private Integer attendeeCount;

    @Column(name = "additional_info", length = 5000)
    private String additionalInfo;

    public CompanyParticipantEntity() {}

    public Long getParticipantId() { return participantId; }
    public void setParticipantId(Long participantId) { this.participantId = participantId; }

    public ParticipantEntity getParticipant() { return participant; }
    public void setParticipant(ParticipantEntity participant) { this.participant = participant; }

    public String getLegalName() { return legalName; }
    public void setLegalName(String legalName) { this.legalName = legalName; }

    public String getRegistryCode() { return registryCode; }
    public void setRegistryCode(String registryCode) { this.registryCode = registryCode; }

    public Integer getAttendeeCount() { return attendeeCount; }
    public void setAttendeeCount(Integer attendeeCount) { this.attendeeCount = attendeeCount; }

    public String getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }
}

