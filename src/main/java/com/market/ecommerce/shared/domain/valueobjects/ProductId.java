package com.market.ecommerce.shared.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record ProductId(UUID value) {

    public ProductId {

        Objects.requireNonNull(value, "Product id is required");

    }

    public static ProductId of(UUID value) {
        return new ProductId(value);
    }

}
