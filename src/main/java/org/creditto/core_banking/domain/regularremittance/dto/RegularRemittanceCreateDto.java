package org.creditto.core_banking.domain.regularremittance.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.creditto.core_banking.domain.regularremittance.entity.ScheduledDay;
import org.creditto.core_banking.global.common.CurrencyCode;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class RegularRemittanceCreateDto {
    private String accountNo;

    private CurrencyCode sendCurrency;
    private CurrencyCode receiveCurrency;
    private BigDecimal sendAmount;

    private String regRemType;      // 매월/매주
    private Integer scheduledDate;  // 매월 - 날짜
    private ScheduledDay scheduledDay;    // 매주 - 요일

    private String recipientName;
    private String recipientPhoneCc;
    private String recipientPhoneNo;
    private String recipientAddress;
    private String recipientCountry;
    private String recipientBankName;
    private String recipientBankCode;
    private String recipientAccountNo;
}
