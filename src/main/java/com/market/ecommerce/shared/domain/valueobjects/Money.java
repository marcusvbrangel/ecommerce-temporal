package com.market.ecommerce.shared.domain.valueobjects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(
        BigDecimal amount,
        CurrencyCode currency) {

    public Money {

        Objects.requireNonNull(amount, "Money amount is required");

        Objects.requireNonNull(currency, "Money currency is required");

        amount = amount.setScale(currency.fractionDigits(), RoundingMode.UNNECESSARY);

    }

    public static Money of(BigDecimal amount, CurrencyCode currency) {
        return new Money(amount, currency);
    }

    public static Money zero(CurrencyCode currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money add(Money other) {

        requereSameCurrency(other);

        return new Money(
                amount.add(other.amount),
                currency
        );

    }

    public Money multiply(Quantity quantity) {

        return new Money(
                amount.multiply(BigDecimal.valueOf(quantity.value())),
                currency
        );

    }

    public boolean isPositive() {
        return amount.signum() > 0;
    }

    private void requereSameCurrency(Money other) {

        Objects.requireNonNull(other, "Other money is required");

        if (!currency.equals(other.currency)) {

            throw new IllegalArgumentException("Cannot operate with different currencies");

        }

    }

}
