package com.guest.registration.system.infrastructure.entity;

import jakarta.persistence.*;

/**
 * JPA entity for private participant details (table: private_participants).
 * Uses shared primary key with ParticipantEntity (participant_id is both PK and FK).
 */
@Entity
@Table(name = "private_participants")
public class PrivateParticipantEntity {

    @Id
    @Column(name = "participant_id")
    private Long participantId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "participant_id")
    private ParticipantEntity participant;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "personal_code", nullable = false, length = 11, unique = true)
    private String personalCode;

    @Column(name = "additional_info", length = 1500)
    private String additionalInfo;

    public PrivateParticipantEntity() {}

    public Long getParticipantId() { return participantId; }
    public void setParticipantId(Long participantId) { this.participantId = participantId; }

    public ParticipantEntity getParticipant() { return participant; }
    public void setParticipant(ParticipantEntity participant) { this.participant = participant; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPersonalCode() { return personalCode; }
    public void setPersonalCode(String personalCode) { this.personalCode = personalCode; }

    public String getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }
}
