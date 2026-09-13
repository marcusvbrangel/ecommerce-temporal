package com.market.ecommerce.shared.domain.valueobjects;

import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

public record CurrencyCode(String value) {

    public CurrencyCode {

        Objects.requireNonNull(value, "Currency code is required");

        value = value.trim().toUpperCase(Locale.ROOT);

        if (value.length() != 3) {

            throw new IllegalArgumentException("Currency code must contain 3 characters");

        }

        try {

            Currency.getInstance(value);

        } catch (IllegalArgumentException exception) {

            throw new IllegalArgumentException("Invalid ISO-4217 currency code: " + value, exception);

        }

    }

    public int fractionDigits() {

        return Currency.getInstance(value).getDefaultFractionDigits();

    }

    public static CurrencyCode of(String value) {
        return new CurrencyCode(value);
    }

    public static CurrencyCode brl() {
        return new CurrencyCode("BRL");
    }

}
