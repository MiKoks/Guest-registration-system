package com.guest.registration.system.infrastructure.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class EventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(name = "event_time", nullable = false)
  private LocalDateTime eventTime;

  @Column(nullable = false, length = 255)
  private String location;

  @Column(name = "additional_info", length = 1000)
  private String additionalInfo;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public LocalDateTime getEventTime() { return eventTime; }
  public void setEventTime(LocalDateTime eventTime) { this.eventTime = eventTime; }

  public String getLocation() { return location; }
  public void setLocation(String location) { this.location = location; }

  public String getAdditionalInfo() { return additionalInfo; }
  public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }
}
