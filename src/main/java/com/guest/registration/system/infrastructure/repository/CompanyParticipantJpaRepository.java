package com.guest.registration.system.infrastructure.repository;

import com.guest.registration.system.infrastructure.entity.CompanyParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyParticipantJpaRepository extends JpaRepository<CompanyParticipantEntity, Long> {
  Optional<CompanyParticipantEntity> findByRegistryCode(String registryCode);
}
