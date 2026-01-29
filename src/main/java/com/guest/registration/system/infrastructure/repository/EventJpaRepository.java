package com.guest.registration.system.infrastructure.repository;

import com.guest.registration.system.infrastructure.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventJpaRepository extends JpaRepository<EventEntity, Long> {
}
