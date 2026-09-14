package com.market.ecommerce.order.domain;

import java.util.Objects;

public record OrderProductName(String value) {

    public OrderProductName {

        Objects.requireNonNull(value, "Product name snapshot is required");

        value = value.trim();

        if (value.isBlank()) {
            throw new IllegalArgumentException("Product name snapshot cannot be blank");
        }

        if (value.length() > 200) {
            throw new IllegalArgumentException("Product name snapshot cannot exceed 200 characters");
        }

    }

}
