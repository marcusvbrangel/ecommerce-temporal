package com.market.ecommerce.shared.domain.event;

import java.time.Instant;

public interface DomainEvent {

    DomainEventId eventId();

    Instant occurredAt();

    String eventType();

}
