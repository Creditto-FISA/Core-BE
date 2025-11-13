package org.creditto.core_banking.domain.overseasremittance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 서비스 계층 내부에서 해외송금 실행에 필요한 데이터를 전달하는 Command 객체입니다.
 * {@link OverseasRemittanceRequestDto}로부터 생성될 수 있으며, 송금 처리 로직에 필요한 모든 정보를 포함합니다.
 */
@Getter
@Builder
@AllArgsConstructor
public class ExecuteRemittanceCommand {

    /**
     * 고객 ID
     */
    private String clientId;

    /**
     * 수취인 ID
     */
    private Long recipientId;

    /**
     * 출금계좌 ID
     */
    private Long accountId;

    /**
     * 수수료 정책 ID
     */
    private Long feeId;

    /**
     * 정기송금 ID (일회성 송금의 경우 null)
     */
    private Long regRemId;

    /**
     * 적용 환율
     */
    private BigDecimal exchangeRate;

    /**
     * 송금 통화 코드 (e.g., "USD")
     */
    private String currencyCode;

    /**
     * 송금액
     */
    private BigDecimal sendAmount;

    /**
     * {@link OverseasRemittanceRequestDto}로부터 {@link ExecuteRemittanceCommand} 객체를 생성합니다.
     *
     * @param request 해외송금 요청 DTO
     * @return 생성된 Command 객체
     */
    public static ExecuteRemittanceCommand from(OverseasRemittanceRequestDto request) {
        return ExecuteRemittanceCommand.builder()
                .clientId(request.getClientId())
                .recipientId(request.getRecipientId())
                .accountId(request.getAccountId())
                .feeId(request.getFeeId())
                .regRemId(null) // 일회성 송금이므로 정기송금 ID는 null로 설정
                .exchangeRate(request.getExchangeRate())
                .currencyCode(request.getCurrencyCode())
                .sendAmount(request.getSendAmount())
                .build();
    }
}
