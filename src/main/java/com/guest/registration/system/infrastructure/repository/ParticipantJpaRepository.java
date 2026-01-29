package com.guest.registration.system.infrastructure.repository;

import com.guest.registration.system.infrastructure.entity.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantJpaRepository extends JpaRepository<ParticipantEntity, Long> {
}
