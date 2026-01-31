package com.guest.registration.system.infrastructure.entity;

import com.guest.registration.system.domain.model.ParticipantType;

import jakarta.persistence.*;

/**
 * JPA entity for base participant record (table: participants).
 * Details are stored in private_participants or company_participants via shared PK.
 */
@Entity
@Table(name = "participants")
public class ParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipantType type;

    @OneToOne(
        mappedBy = "participant",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    private PrivateParticipantEntity privateDetails;

    @OneToOne(
        mappedBy = "participant",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    private CompanyParticipantEntity companyDetails;

    public ParticipantEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ParticipantType getType() { return type; }
    public void setType(ParticipantType type) { this.type = type; }

    public PrivateParticipantEntity getPrivateDetails() { return privateDetails; }

    public void setPrivateDetails(PrivateParticipantEntity privateDetails) {
        this.privateDetails = privateDetails;
        if (privateDetails != null) {
            privateDetails.setParticipant(this);
        }
    }

    public CompanyParticipantEntity getCompanyDetails() { return companyDetails; }

    public void setCompanyDetails(CompanyParticipantEntity companyDetails) {
        this.companyDetails = companyDetails;
        if (companyDetails != null) {
            companyDetails.setParticipant(this);
        }
    }
}

