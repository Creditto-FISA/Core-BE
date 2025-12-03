package org.creditto.core_banking.domain.exchange.dto;

import java.math.BigDecimal;

public record PreferentialRateRes(
    double preferentialRate,
    BigDecimal appliedRate
) {
}