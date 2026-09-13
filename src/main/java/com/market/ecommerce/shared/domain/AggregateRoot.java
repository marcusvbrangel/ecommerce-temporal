package com.market.ecommerce.shared.domain;

import com.market.ecommerce.shared.domain.event.DomainEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AggregateRoot {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected final void registerDomainEvent(DomainEvent event) {

        Objects.requireNonNull(event);

        domainEvents.add(event);

    }

    public final List<DomainEvent> pullDomainEvents() {

        List<DomainEvent> events = List.copyOf(domainEvents);

        domainEvents.clear();

        return events;

    }

    public final List<DomainEvent> domainEvents() {

        return List.copyOf(domainEvents);

    }

}
