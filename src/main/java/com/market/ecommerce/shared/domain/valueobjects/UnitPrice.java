package com.market.ecommerce.shared.domain.valueobjects;

import java.util.Objects;

public record UnitPrice(Money value) {

    public UnitPrice {

        Objects.requireNonNull(value, "Unit price is required");

        if (!value().isPositive()) {
            throw new IllegalArgumentException("Unit price must be greater than zero");
        }

    }

    public static UnitPrice of(Money money) {
        return new UnitPrice(money);
    }

}
