package org.creditto.core_banking.domain.remittancefee.service;

import org.creditto.core_banking.domain.remittancefee.dto.RemittanceFeeReq;
import org.creditto.core_banking.domain.remittancefee.entity.FlatServiceFee;
import org.creditto.core_banking.domain.remittancefee.entity.NetworkFee;
import org.creditto.core_banking.domain.remittancefee.repository.FeeRecordRepository;
import org.creditto.core_banking.domain.remittancefee.repository.FlatServiceFeeRepository;
import org.creditto.core_banking.domain.remittancefee.repository.NetworkFeeRepository;
import org.creditto.core_banking.domain.remittancefee.repository.PctServiceFeeRepository;
import org.creditto.core_banking.domain.remittancefee.entity.FeeRecord;
import org.creditto.core_banking.domain.remittancefee.entity.PctServiceFee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemittanceFeeServiceTest {

    @InjectMocks
    private RemittanceFeeService remittanceFeeService;

    @Mock
    private FlatServiceFeeRepository flatServiceFeeRepository;
    @Mock
    private PctServiceFeeRepository pctServiceFeeRepository;
    @Mock
    private NetworkFeeRepository networkFeeRepository;
    @Mock
    private FeeRecordRepository feeRecordRepository;

    @Test
    @DisplayName("USD 1000달러 송금 시 수수료 계산 및 저장 (PctFee 비활성)")
    void calculateAndSaveFee_Success_when_PctFeeInactive() {
        // given
        BigDecimal sendAmount = new BigDecimal("1000");
        BigDecimal exchangeRate = new BigDecimal("1500");
        String currency = "USD";
        RemittanceFeeReq req = new RemittanceFeeReq(exchangeRate, sendAmount, currency);

        // Mocking: FlatServiceFee (1000달러는 3000달러 이하 구간, feeAmount 7500원)
        FlatServiceFee flatFeePolicy = FlatServiceFee.of(2L, new BigDecimal("3000"), new BigDecimal("7500"));
        when(flatServiceFeeRepository.findFirstByUpperLimitGreaterThanEqualOrderByUpperLimitAsc(sendAmount))
            .thenReturn(Optional.of(flatFeePolicy));

        // Mocking: PctServiceFee (isActive=false인 정책 반환)
        PctServiceFee pctFeePolicy = PctServiceFee.of(1L, new BigDecimal("0.2"), false);
        when(pctServiceFeeRepository.findFirstByOrderByPctServiceFeeIdAsc()).thenReturn(Optional.of(pctFeePolicy));

        // Mocking: NetworkFee (USD는 15달러)
        NetworkFee networkFeePolicy = NetworkFee.of(1L, "USD", new BigDecimal("15"));
        when(networkFeeRepository.findByCurrencyCode(currency)).thenReturn(Optional.of(networkFeePolicy));

        // Mocking: feeRecordRepository.save
        when(feeRecordRepository.save(any(FeeRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        remittanceFeeService.calculateAndSaveFee(req);

        // then
        ArgumentCaptor<FeeRecord> feeRecordCaptor = ArgumentCaptor.forClass(FeeRecord.class);
        verify(feeRecordRepository).save(feeRecordCaptor.capture());
        FeeRecord savedFeeRecord = feeRecordCaptor.getValue();

        // flatFeeInKRW = 7500 (KRW 기준)
        // pctFeeInKRW = 0 (isActive=false)
        // networkFeeInKRW = 15 * 1500 = 22500
        // totalFee = 7500 + 0 + 22500 = 30000
        BigDecimal expectedTotalFee = new BigDecimal("30000");
        assertEquals(0, expectedTotalFee.compareTo(savedFeeRecord.getTotalFee()));
    }
}
