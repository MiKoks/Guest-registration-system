package com.guest.registration.system.application.dto;

import com.guest.registration.system.domain.model.ParticipantType;

public class ParticipantDetailsDTO {

    private Long id;
    private ParticipantType type;

    // PRIVATE fields
    private String firstName;
    private String lastName;
    private String personalCode;
    private String privateAdditionalInfo;

    // COMPANY fields
    private String legalName;
    private String registryCode;
    private Integer attendeeCount;
    private String companyAdditionalInfo;

    public ParticipantDetailsDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ParticipantType getType() { return type; }
    public void setType(ParticipantType type) { this.type = type; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPersonalCode() { return personalCode; }
    public void setPersonalCode(String personalCode) { this.personalCode = personalCode; }

    public String getPrivateAdditionalInfo() { return privateAdditionalInfo; }
    public void setPrivateAdditionalInfo(String privateAdditionalInfo) { this.privateAdditionalInfo = privateAdditionalInfo; }

    public String getLegalName() { return legalName; }
    public void setLegalName(String legalName) { this.legalName = legalName; }

    public String getRegistryCode() { return registryCode; }
    public void setRegistryCode(String registryCode) { this.registryCode = registryCode; }

    public Integer getAttendeeCount() { return attendeeCount; }
    public void setAttendeeCount(Integer attendeeCount) { this.attendeeCount = attendeeCount; }

    public String getCompanyAdditionalInfo() { return companyAdditionalInfo; }
    public void setCompanyAdditionalInfo(String companyAdditionalInfo) { this.companyAdditionalInfo = companyAdditionalInfo; }
}

