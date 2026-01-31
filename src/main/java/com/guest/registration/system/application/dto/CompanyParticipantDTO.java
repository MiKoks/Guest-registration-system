package com.guest.registration.system.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CompanyParticipantDTO {

    @NotBlank
    @Size(max = 255)
    private String legalName;

    @NotBlank
    @Size(max = 50)
    private String registryCode;

    @Min(1)
    private int attendeeCount;

    @Size(max = 5000)
    private String additionalInfo;

    // getters & setters
    public String getLegalName() { return legalName; }
    public void setLegalName(String legalName) { this.legalName = legalName; }

    public String getRegistryCode() { return registryCode; }
    public void setRegistryCode(String registryCode) { this.registryCode = registryCode; }

    public int getAttendeeCount() { return attendeeCount; }
    public void setAttendeeCount(int attendeeCount) { this.attendeeCount = attendeeCount; }

    public String getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }
}

