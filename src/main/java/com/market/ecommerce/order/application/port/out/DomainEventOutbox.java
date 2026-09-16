package com.market.ecommerce.order.application.port.out;

import com.market.ecommerce.shared.domain.event.DomainEvent;

import java.util.List;

public interface DomainEventOutbox {

    void appendAll(List<DomainEvent> events);

}
