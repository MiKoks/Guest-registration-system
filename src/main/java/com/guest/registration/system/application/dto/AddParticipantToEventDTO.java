package com.guest.registration.system.application.dto;

import jakarta.validation.constraints.*;

public class AddParticipantToEventDTO {

    @NotBlank(message = "Osaleja tüüp on kohustuslik")
    @Pattern(regexp = "PRIVATE|COMPANY", message = "Osaleja tüüp peab olema PRIVATE või COMPANY")
    private String participantType;

    private Long participantId;

    // PRIVATE
    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Size(min = 11, max = 11)
    private String personalCode;

    // COMPANY
    @Size(max = 255)
    private String legalName;

    @Size(max = 50)
    private String registryCode;

    @Min(value = 1, message = "Osavõtjate arv peab olema vähemalt 1")
    private Integer attendeeCount;

    // Registration
    @NotBlank(message = "Makseviis on kohustuslik")
    private String paymentMethodCode; // BANK_TRANSFER / CASH

    @Size(max = 5000)
    private String additionalInfo;

    public String getParticipantType() { return participantType; }
    public void setParticipantType(String participantType) { this.participantType = participantType; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPersonalCode() { return personalCode; }
    public void setPersonalCode(String personalCode) { this.personalCode = personalCode; }

    public String getLegalName() { return legalName; }
    public void setLegalName(String legalName) { this.legalName = legalName; }

    public String getRegistryCode() { return registryCode; }
    public void setRegistryCode(String registryCode) { this.registryCode = registryCode; }

    public Integer getAttendeeCount() { return attendeeCount; }
    public void setAttendeeCount(Integer attendeeCount) { this.attendeeCount = attendeeCount; }

    public String getPaymentMethodCode() { return paymentMethodCode; }
    public void setPaymentMethodCode(String paymentMethodCode) { this.paymentMethodCode = paymentMethodCode; }

    public String getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }

    public Long getParticipantId() { return participantId; }
    public void setParticipantId(Long participantId) { this.participantId = participantId; }
}

