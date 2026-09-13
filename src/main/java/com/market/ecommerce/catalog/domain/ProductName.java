package com.market.ecommerce.catalog.domain;

import java.util.Objects;

public record ProductName(String value) {

    public ProductName {

        Objects.requireNonNull(value, "Product name is required");

        value = value.trim();

        if (value.isBlank()) {

            throw new IllegalArgumentException("Product name cannot be blank");

        }

        if (value.length() > 200) {

            throw new IllegalArgumentException("Product name cannot exceed 200 characters");

        }


    }
}
