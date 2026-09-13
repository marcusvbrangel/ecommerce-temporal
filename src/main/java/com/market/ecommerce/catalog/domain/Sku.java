package com.market.ecommerce.catalog.domain;

import java.util.Locale;
import java.util.Objects;

public record Sku(String value) {

    public Sku {

        Objects.requireNonNull(value, "SKU is required");

        value = value.trim().toUpperCase(Locale.ROOT);

        if (value.isBlank()) {

            throw new IllegalArgumentException("SKU cannot be blank");

        }

        if (value.length() > 40) {

            throw new IllegalArgumentException("SKU cannot exceed 40 characters");

        }

    }

}
