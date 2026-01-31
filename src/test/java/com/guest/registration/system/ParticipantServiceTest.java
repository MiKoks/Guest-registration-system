package com.guest.registration.system;

import com.guest.registration.system.application.dto.CompanyParticipantDTO;
import com.guest.registration.system.application.dto.PrivateParticipantDTO;
import com.guest.registration.system.application.service.ParticipantService;
import com.guest.registration.system.domain.validation.DomainValidationException;
import com.guest.registration.system.infrastructure.repository.CompanyParticipantJpaRepository;
import com.guest.registration.system.infrastructure.repository.ParticipantJpaRepository;
import com.guest.registration.system.infrastructure.repository.PrivateParticipantJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ParticipantServiceTest {

    @Autowired private ParticipantService participantService;
    @Autowired private ParticipantJpaRepository participantRepo;
    @Autowired private PrivateParticipantJpaRepository privateRepo;
    @Autowired private CompanyParticipantJpaRepository companyRepo;

    @BeforeEach
    void clean() {
        // order matters (shared PK tables)
        privateRepo.deleteAll();
        companyRepo.deleteAll();
        participantRepo.deleteAll();
    }

    @Test
    void createPrivateParticipant_shouldPersist() {
        PrivateParticipantDTO dto = new PrivateParticipantDTO();
        dto.setFirstName("Mari");
        dto.setLastName("Tamm");
        dto.setPersonalCode(generateValidEstonianPersonalCode());
        dto.setAdditionalInfo("Soovid...");

        Long id = participantService.createPrivateParticipant(dto);

        assertThat(id).isNotNull();
        assertThat(participantRepo.findById(id)).isPresent();
        assertThat(privateRepo.findById(id)).isPresent();
    }

    @Test
    void createPrivateParticipant_invalidPersonalCode_shouldFail() {
        PrivateParticipantDTO dto = new PrivateParticipantDTO();
        dto.setFirstName("Mari");
        dto.setLastName("Tamm");
        dto.setPersonalCode("12345678901"); // almost certainly invalid checksum
        dto.setAdditionalInfo("x");

        assertThatThrownBy(() -> participantService.createPrivateParticipant(dto))
            .isInstanceOf(DomainValidationException.class)
            .hasMessageContaining("personal code");
    }

    @Test
    void createCompanyParticipant_shouldPersist() {
        CompanyParticipantDTO dto = new CompanyParticipantDTO();
        dto.setLegalName("Näide OÜ");
        dto.setRegistryCode("12345678");
        dto.setAttendeeCount(3);
        dto.setAdditionalInfo("Ettevõtte soovid...");

        Long id = participantService.createCompanyParticipant(dto);

        assertThat(id).isNotNull();
        assertThat(participantRepo.findById(id)).isPresent();
        assertThat(companyRepo.findById(id)).isPresent();
    }

    // -------- helper: generate valid code (only checksum validity) --------

    private static String generateValidEstonianPersonalCode() {
        Random r = new Random();
        int[] digits = new int[11];

        // first 10 digits random (must be digits)
        for (int i = 0; i < 10; i++) digits[i] = r.nextInt(10);

        int check = computeCheckDigit(digits);
        digits[10] = check;

        StringBuilder sb = new StringBuilder(11);
        for (int d : digits) sb.append(d);
        return sb.toString();
    }

    private static int computeCheckDigit(int[] digits11) {
        int[] w1 = {1,2,3,4,5,6,7,8,9,1};
        int[] w2 = {3,4,5,6,7,8,9,1,2,3};

        int mod1 = weightedMod(digits11, w1);
        if (mod1 != 10) return mod1;

        int mod2 = weightedMod(digits11, w2);
        if (mod2 != 10) return mod2;

        return 0;
    }

    private static int weightedMod(int[] digits11, int[] weights10) {
        int sum = 0;
        for (int i = 0; i < 10; i++) sum += digits11[i] * weights10[i];
        return sum % 11;
    }
}

