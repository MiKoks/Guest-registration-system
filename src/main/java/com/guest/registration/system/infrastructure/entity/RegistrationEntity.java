package com.guest.registration.system.infrastructure.entity;

import jakarta.persistence.*;

/**
 * JPA entity for registrations (table: registrations).
 * Links Event + Participant + PaymentMethod.
 */
@Entity
@Table(
    name = "registrations",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_event_participant", columnNames = {"event_id", "participant_id"})
    }
)
public class RegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participant_id", nullable = false)
    private ParticipantEntity participant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethodEntity paymentMethod;

    @Column(name = "additional_info", length = 5000)
    private String additionalInfo;

    public RegistrationEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EventEntity getEvent() { return event; }
    public void setEvent(EventEntity event) { this.event = event; }

    public ParticipantEntity getParticipant() { return participant; }
    public void setParticipant(ParticipantEntity participant) { this.participant = participant; }

    public PaymentMethodEntity getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethodEntity paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }
}
