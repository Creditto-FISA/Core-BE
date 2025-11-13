package org.creditto.core_banking.domain.remittancefee.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.creditto.core_banking.global.common.BaseEntity;

import java.math.BigDecimal;

@Entity
@Getter
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class NetworkFee extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long networkFeeId;

    // TODO: 통화 코드 Enum 타입으로 변경
    private String currencyCode;

    private BigDecimal feeAmount;

    public static NetworkFee of(
            Long networkFeeId,
            String currencyCode,
            BigDecimal feeAmount
    ) {
        return NetworkFee.builder()
                .networkFeeId(networkFeeId)
                .currencyCode(currencyCode)
                .feeAmount(feeAmount)
                .build();
    }

}
