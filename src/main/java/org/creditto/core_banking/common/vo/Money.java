package org.creditto.core_banking.common.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.creditto.core_banking.global.common.CurrencyCode;
import org.creditto.core_banking.global.response.error.ErrorBaseCode;
import org.creditto.core_banking.global.response.exception.CustomBaseException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@EqualsAndHashCode
public final class Money {
    private final BigDecimal amount;

    private final CurrencyCode currency;

    private Money(final BigDecimal amount, final CurrencyCode currency) {
        if (currency == null) {
            throw new CustomBaseException(ErrorBaseCode.CURRENCY_NOT_SUPPORTED);
        }
        if (amount == null || amount.signum() < 0) {
            throw new CustomBaseException(ErrorBaseCode.BAD_REQUEST);
        }
        this.amount = normalize(amount, currency);
        this.currency = currency;
    }

    public static Money of(BigDecimal amount, CurrencyCode currency) {
        return new Money(amount, currency);
    }

    public Money plus(Money other) {
        assertSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money minus(Money other) {
        assertSameCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.signum() < 0) {
            throw new CustomBaseException(ErrorBaseCode.INSUFFICIENT_FUNDS);
        }
        return new Money(result, this.currency);
    }

    public boolean gte(Money other) {
        assertSameCurrency(other);
        return this.amount.compareTo(other.amount) >= 0;
    }

    private void assertSameCurrency(Money other) {
        if (other == null) {
            throw new CustomBaseException(ErrorBaseCode.BAD_REQUEST);
        }
        if (this.currency != other.currency) {
            throw new CustomBaseException(ErrorBaseCode.CURRENCY_NOT_SUPPORTED);
        }
    }

    private static BigDecimal normalize(BigDecimal value, CurrencyCode currency) {
        int scale = (currency == CurrencyCode.KRW) ? 0 : 2;
        return value.setScale(scale, RoundingMode.HALF_UP);
    }
}
