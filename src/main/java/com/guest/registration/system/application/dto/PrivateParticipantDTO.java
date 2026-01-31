package com.guest.registration.system.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PrivateParticipantDTO {

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @NotBlank
    @Size(min = 11, max = 11)
    private String personalCode;

    @Size(max = 1500)
    private String additionalInfo;

    // getters & setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPersonalCode() { return personalCode; }
    public void setPersonalCode(String personalCode) { this.personalCode = personalCode; }

    public String getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }
}

