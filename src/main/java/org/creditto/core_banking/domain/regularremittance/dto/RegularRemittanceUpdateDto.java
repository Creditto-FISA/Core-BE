package org.creditto.core_banking.domain.regularremittance.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.creditto.core_banking.domain.regularremittance.entity.RegRemStatus;
import org.creditto.core_banking.domain.regularremittance.entity.ScheduledDay;
import org.creditto.core_banking.global.common.CurrencyCode; // CurrencyCode import 추가

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class RegularRemittanceUpdateDto {

    private String accountNo;
    private BigDecimal sendAmount;
    private RegRemStatus regRemStatus;
    private Integer scheduledDate;  // 매월일 경우 수정할 날짜
    private ScheduledDay scheduledDay;    // 매주일 경우 수정할 요일
}
