package org.creditto.core_banking.common.vo;

import org.creditto.core_banking.global.common.CurrencyCode;
import org.creditto.core_banking.global.response.exception.CustomBaseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    @DisplayName("같은 통화의 금액은 더할 수 있다")
    void plus_sameCurrency_success() {
        Money a = Money.of(new BigDecimal("1000"), CurrencyCode.KRW);
        Money b = Money.of(new BigDecimal("2000"), CurrencyCode.KRW);

        Money result = a.plus(b);

        assertThat(result.getAmount()).isEqualByComparingTo("3000");
        assertThat(result.getCurrency()).isEqualTo(CurrencyCode.KRW);
    }

    @Test
    @DisplayName("잔액보다 큰 금액을 빼면 예외가 발생한다")
    void minus_insufficientFunds_fail() {
        Money a = Money.of(new BigDecimal("1000"), CurrencyCode.KRW);
        Money b = Money.of(new BigDecimal("1001"), CurrencyCode.KRW);

        assertThatThrownBy(() -> a.minus(b))
                .isInstanceOf(CustomBaseException.class);
    }

    @Test
    @DisplayName("통화가 다르면 연산할 수 없다")
    void plus_differentCurrency_fail() {
        Money krw = Money.of(new BigDecimal("1000"), CurrencyCode.KRW);
        Money usd = Money.of(new BigDecimal("1"), CurrencyCode.USD);

        assertThatThrownBy(() -> krw.plus(usd))
                .isInstanceOf(CustomBaseException.class);
    }

    @Test
    @DisplayName("KRW는 소수점 없이 정규화된다")
    void of_krw_normalizeScale_zero() {
        Money krw = Money.of(new BigDecimal("1000.6"), CurrencyCode.KRW);

        assertThat(krw.getAmount()).isEqualByComparingTo("1001");
    }
}
