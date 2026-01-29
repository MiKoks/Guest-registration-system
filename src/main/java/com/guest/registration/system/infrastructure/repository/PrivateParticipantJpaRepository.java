package com.guest.registration.system.infrastructure.repository;

import com.guest.registration.system.infrastructure.entity.PrivateParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivateParticipantJpaRepository extends JpaRepository<PrivateParticipantEntity, Long> {
  Optional<PrivateParticipantEntity> findByPersonalCode(String personalCode);
}
