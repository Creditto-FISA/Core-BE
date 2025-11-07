package org.creditto.core_banking.domain.overseasremittance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class OverseasRemittanceRequestDto {

    private String clientId;      // 고객 ID
    private Long recipientId;     // 수취인 ID
    private Long accountId;       // 출금계좌 ID
    private Long feeId;           // 수수료 정책 ID
    private Long regRemId;        // 정기송금 ID (선택)
    private Double exchangeRate;  // 환율
    private String currencyCode;   // 통화코드
    private BigDecimal sendAmount; // 송금금액
    private BigDecimal receivedAmount; // 송금금액
}
