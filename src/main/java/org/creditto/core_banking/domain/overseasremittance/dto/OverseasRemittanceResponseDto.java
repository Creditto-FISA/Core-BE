package org.creditto.core_banking.domain.overseasremittance.dto;

import lombok.Builder;
import lombok.Getter;
import org.creditto.core_banking.domain.overseasremittance.entity.OverseasRemittance;
import org.creditto.core_banking.domain.overseasremittance.entity.RemittanceStatus;

import java.math.BigDecimal;

/**
 * 해외송금 처리 결과 및 조회 결과를 클라이언트에게 반환하기 위한 DTO(Data Transfer Object)입니다.
 */
@Getter
@Builder
public class OverseasRemittanceResponseDto {

    /**
     * 송금 ID
     */
    private Long remittanceId;

    /**
     * 수취인 이름
     */
    private String recipientName;

    /**
     * 출금 계좌 번호
     */
    private String accountNo;

    /**
     * 고객 ID
     */
    private String clientId;

    /**
     * 적용된 수수료 정책 ID
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
     * 송금액
     */
    private BigDecimal sendAmount;

    /**
     * 최종 수취 금액
     */
    private BigDecimal receiveAmount;

    /**
     * 송금 처리 상태
     */
    private RemittanceStatus remittanceStatus;

    /**
     * {@link OverseasRemittance} 엔티티로부터 {@link OverseasRemittanceResponseDto} 객체를 생성합니다.
     *
     * @param overseasRemittance 해외송금 엔티티
     * @return 생성된 DTO 객체
     */
    public static OverseasRemittanceResponseDto from(OverseasRemittance overseasRemittance) {
        return OverseasRemittanceResponseDto.builder()
                .remittanceId(overseasRemittance.getRemittanceId())
                .recipientName(overseasRemittance.getRecipient().getName())
                .accountNo(overseasRemittance.getAccount().getAccountNo())
                .clientId(overseasRemittance.getClientId())
                .feeId(overseasRemittance.getFee().getFeeId())
                .regRemId(overseasRemittance.getRecur() != null
                        ? overseasRemittance.getRecur().getRegRemId()
                        : null)        // 정기송금이 아닌경우 null 반환
                .exchangeRate(overseasRemittance.getExchangeRate())
                .sendAmount(overseasRemittance.getSendAmount())
                .receiveAmount(overseasRemittance.getReceiveAmount())
                .remittanceStatus(overseasRemittance.getRemittanceStatus())
                .build();
    }

}

