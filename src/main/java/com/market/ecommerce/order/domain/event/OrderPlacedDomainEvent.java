package com.market.ecommerce.order.domain.event;

import com.market.ecommerce.order.domain.CustomerId;
import com.market.ecommerce.order.domain.OrderId;
import com.market.ecommerce.shared.domain.event.DomainEvent;
import com.market.ecommerce.shared.domain.event.DomainEventId;
import com.market.ecommerce.shared.domain.valueobjects.Money;

import java.time.Instant;
import java.util.Objects;

public record OrderPlacedDomainEvent(

        DomainEventId eventId,

        OrderId orderId,

        CustomerId customerId,

        Money total,

        int itemCount,

        Instant occurredAt

) implements DomainEvent {

    public OrderPlacedDomainEvent {

        Objects.requireNonNull(eventId);

        Objects.requireNonNull(orderId);

        Objects.requireNonNull(customerId);

        Objects.requireNonNull(total);

        Objects.requireNonNull(occurredAt);

        if (itemCount <= 0) {

            throw new IllegalArgumentException("Order placed event must contain items");

        }

    }

    @Override
    public String eventType() {

        return "order.placed.v1";

    }

}
