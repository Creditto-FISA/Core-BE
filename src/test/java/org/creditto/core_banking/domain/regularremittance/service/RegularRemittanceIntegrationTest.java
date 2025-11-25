package org.creditto.core_banking.domain.regularremittance.service;

import org.creditto.core_banking.domain.account.entity.Account;
import org.creditto.core_banking.domain.account.entity.AccountState;
import org.creditto.core_banking.domain.account.entity.AccountType;
import org.creditto.core_banking.domain.account.repository.AccountRepository;
import org.creditto.core_banking.domain.regularremittance.dto.RegularRemittanceResponseDto;
import org.creditto.core_banking.domain.regularremittance.entity.MonthlyRegularRemittance;
import org.creditto.core_banking.domain.regularremittance.repository.RegularRemittanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@Transactional
class RegularRemittanceIntegrationTest {

    @Autowired
    private RegularRemittanceRepository regularRemittanceRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RegularRemittanceService regularRemittanceService; // 서비스 메서드를 직접 호출해보기 위해

    private Long testUserId = 3L;
    private Account testAccount;

    @BeforeEach
    void setup() {
        // 기존 데이터를 깨끗하게 정리 (만약 @Transactional이 없거나 다른 테스트와 섞일 경우)
        // regularRemittanceRepository.deleteAll();
        // accountRepository.deleteAll();

        // 테스트용 Account 생성 및 저장
        testAccount = Account.of(
                "1234567890123", // accountNo (실제 계좌번호 생성 로직에 따라 다름)
                "Test User Account",
                BigDecimal.valueOf(100000),
                AccountType.DEPOSIT, // AccountType.DEPOSIT
                AccountState.ACTIVE, // AccountState.ACTIVE
                testUserId
        );
        accountRepository.save(testAccount);

        // 테스트용 MonthlyRegularRemittance 생성 및 저장 (Monthly 예시)
        MonthlyRegularRemittance monthlyRemittance = MonthlyRegularRemittance.of(
                testAccount,
                null, // recipient는 지금 중요하지 않으므로 null
                null, // currency는 지금 중요하지 않으므로 null
                null, // currency
                BigDecimal.valueOf(1000),
                15 // scheduledDate
        );
        regularRemittanceRepository.save(monthlyRemittance);
    }

    @Test
    void shouldFindScheduledRemittancesByUserId() {
        // 서비스 메서드를 통해 조회
        List<RegularRemittanceResponseDto> result = regularRemittanceService.getScheduledRemittancesByUserId(testUserId);

        // 결과 검증
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1); // 1개의 더미 데이터를 넣었으므로 1개여야 함
        RegularRemittanceResponseDto foundRemittance = result.get(0);
//        assertThat(foundRemittance.getSendAmount()).isEqualByComparingTo(new BigDecimal("1000"));
        assertThat(foundRemittance.getScheduledDate()).isEqualTo(15);
        assertThat(foundRemittance.getRegRemType()).isEqualTo("MONTHLY");
    }
}