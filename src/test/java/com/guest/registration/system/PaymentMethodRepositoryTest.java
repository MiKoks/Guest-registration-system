package com.guest.registration.system;

import com.guest.registration.system.infrastructure.repository.PaymentMethodJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PaymentMethodRepositoryTest {
    
    @Autowired
    private PaymentMethodJpaRepository paymentMethodRepo;

    @Test
    void LoadSeededPaymentMethods() {
        var all = paymentMethodRepo.findAll();

        assertThat(all).extracting("code")
            .contains("BANK_TRANSFER", "CASH");
    }

    @Test
    void shouldReturnOnlyActiveMethods() {
        var active = paymentMethodRepo.findByActiveTrueOrderByDisplayNameAsc();
        assertThat(active).isNotEmpty();
        assertThat(active).allMatch(pm -> pm.isActive());
    }
}
