package com.market.ecommerce.order.domain;

import java.util.Objects;
import java.util.UUID;

public record CustomerId(UUID value) {

    public CustomerId {

        Objects.requireNonNull(value, "Customer id is required");

    }

    public static CustomerId of(UUID value) {
        return new CustomerId(value);
    }

}
