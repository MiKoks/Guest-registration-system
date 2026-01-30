package com.guest.registration.system.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Event.
 */
public class EventDTO {
    
    private Long id;
    
    @NotBlank(message = "Ürituse nimi on kohustuslik")
    @Size(max = 255, message = "Ürituse nimi ei tohi olla pikem kui 255 tähemärki")
    private String name;
    
    @Future(message = "Toimumisaeg peab olema tulevikus")
    @NotNull(message = "Toimumisaeg on kohustuslik")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime eventTime;
    
    @NotBlank(message = "Toimumise koht on kohustuslik")
    @Size(max = 255, message = "Toimumise koht ei tohi olla pikem kui 255 tähemärki")
    private String location;
    
    @Size(max = 1000, message = "Lisainfo ei tohi olla pikem kui 1000 tähemärki")
    private String additionalInfo;
    
    private Integer participantCount;
    private Boolean futureEvent;
    
    // Constructors
    public EventDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public LocalDateTime getEventDateTime() { return eventTime; }
    public void setEventDateTime(LocalDateTime eventDateTime) { this.eventTime = eventDateTime; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }
    
    public Integer getParticipantCount() { return participantCount; }
    public void setParticipantCount(Integer participantCount) { this.participantCount = participantCount; }
    
    public Boolean getFutureEvent() { return futureEvent; }
    public void setFutureEvent(Boolean futureEvent) { this.futureEvent = futureEvent; }
}
