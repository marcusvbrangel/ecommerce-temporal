package com.market.ecommerce.shared.domain.valueobjects;

public record Quantity(int value) {

    public Quantity {

        if (value <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

    }

    public static Quantity of(int value) {
        return new Quantity(value);
    }

}
