package com.market.ecommerce.order.adapter.out.event;

import com.market.ecommerce.order.application.port.out.DomainEventOutbox;
import com.market.ecommerce.order.domain.event.OrderPlacedDomainEvent;
import com.market.ecommerce.shared.domain.event.DomainEvent;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.json.JsonMapper;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcDomainEventOutbox implements DomainEventOutbox {

    private final JdbcClient jdbcClient;
    private final JsonMapper jsonMapper;

    public JdbcDomainEventOutbox(JdbcClient jdbcClient, JsonMapper jsonMapper) {
        this.jdbcClient = jdbcClient;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void appendAll(List<DomainEvent> events) {

        for (DomainEvent event : events) {
            append(event);
        }

    }

    private void append(DomainEvent event) {

        if (event instanceof OrderPlacedDomainEvent orderPlaced) {

            appendOrderPlaced(orderPlaced);

            return;
        }

        throw new IllegalArgumentException("Unsupported domain event: " + event.getClass().getName());

    }

    private void appendOrderPlaced(OrderPlacedDomainEvent event) {

        Map<String, Object> payload = Map.of(
                        "orderId", event.orderId().value().toString(),
                        "customerId", event.customerId().value().toString(),
                        "totalAmount", event.total().amount(),
                        "currency", event.total().currency().value(),
                        "itemCount", event.itemCount()
                );

        String json = toJson(payload);

        String sql = """
                INSERT INTO kafka.domain_event_outbox
                (
                    event_id,
                    aggregate_type,
                    aggregate_id,
                    event_type,
                    payload,
                    occurred_at,
                    status,
                    attempt_count
                )
                VALUES
                (
                    :eventId,
                    :aggregateType,
                    :aggregateId,
                    :eventType,
                    CAST(:payload AS jsonb),
                    :occurredAt,
                    'PENDING',
                    0
                )
                """;

        jdbcClient
                .sql(sql)
                .param("eventId", event.eventId().value())
                .param("aggregateType", "Order")
                .param("aggregateId", event.orderId().value())
                .param("eventType", event.eventType())
                .param("payload", json)
                .param("occurredAt", OffsetDateTime.ofInstant(event.occurredAt(), ZoneOffset.UTC))
                .update();

    }

    private String toJson(Object value) {

        try {

            return jsonMapper.writeValueAsString(value);

        } catch (Exception exception) {

            throw new IllegalStateException("Could not serialize domain event", exception);

        }

    }

}
