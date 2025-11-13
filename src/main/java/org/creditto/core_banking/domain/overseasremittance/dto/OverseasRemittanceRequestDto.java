package org.creditto.core_banking.domain.overseasremittance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 클라이언트로부터 해외송금 요청을 받기 위한 DTO(Data Transfer Object)입니다.
 * 각 필드는 {@link jakarta.validation.constraints} 애노테이션을 통해 유효성 검사가 수행됩니다.
 */
@Getter
@Builder
@AllArgsConstructor
public class OverseasRemittanceRequestDto {

    /**
     * 고객 ID
     */
    @NotBlank(message = "고객 ID는 필수입니다.")
    private String clientId;

    /**
     * 수취인 ID
     */
    @NotNull(message = "수취인 ID는 필수입니다.")
    private Long recipientId;

    /**
     * 출금계좌 ID
     */
    @NotNull(message = "출금계좌 ID는 필수입니다.")
    private Long accountId;

    /**
     * 수수료 정책 ID
     */
    @NotNull(message = "수수료 정책 ID는 필수입니다.")
    private Long feeId;

    /**
     * 정기송금 ID (선택 사항)
     */
    private Long regRemId;

    /**
     * 적용 환율
     */
    @NotNull(message = "환율은 필수입니다.")
    @Positive(message = "환율은 0보다 커야 합니다.")
    private BigDecimal exchangeRate;

    /**
     * 송금 통화 코드 (e.g., "USD")
     */
    @NotBlank(message = "통화코드는 필수입니다.")
    private String currencyCode;

    /**
     * 송금액
     */
    @NotNull(message = "송금액은 필수입니다.")
    @Positive(message = "송금액은 0보다 커야 합니다.")
    private BigDecimal sendAmount;
}
