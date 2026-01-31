package com.guest.registration.system.infrastructure.repository;

import com.guest.registration.system.infrastructure.entity.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodJpaRepository extends JpaRepository<PaymentMethodEntity, Long> {
  List<PaymentMethodEntity> findByActiveTrueOrderByDisplayNameAsc();
  Optional<PaymentMethodEntity> findByCode(String code);

  Optional<PaymentMethodEntity> findByCodeAndActiveTrue(String code);
}

