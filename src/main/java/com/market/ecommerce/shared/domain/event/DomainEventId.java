package com.market.ecommerce.shared.domain.event;

import java.util.Objects;
import java.util.UUID;

public record DomainEventId(UUID value) {

    public DomainEventId {

        Objects.requireNonNull(value, "Domain event id is required");

    }

    public static DomainEventId newId() {
        return new DomainEventId(UUID.randomUUID());
    }

}
