package com.guest.registration.system.infrastructure.repository;

import com.guest.registration.system.infrastructure.entity.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationJpaRepository extends JpaRepository<RegistrationEntity, Long> {
  List<RegistrationEntity> findByEvent_Id(Long eventId);
  Optional<RegistrationEntity> findByEvent_IdAndParticipant_Id(Long eventId, Long participantId);
  long countByEvent_Id(Long eventId);
}
