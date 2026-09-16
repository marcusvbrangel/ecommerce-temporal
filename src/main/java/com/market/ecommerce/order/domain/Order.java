package com.market.ecommerce.order.domain;

import com.market.ecommerce.order.domain.event.OrderPlacedDomainEvent;
import com.market.ecommerce.shared.domain.AggregateRoot;
import com.market.ecommerce.shared.domain.event.DomainEventId;
import com.market.ecommerce.shared.domain.valueobjects.CurrencyCode;
import com.market.ecommerce.shared.domain.valueobjects.Money;
import com.market.ecommerce.shared.domain.valueobjects.ProductId;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class Order extends AggregateRoot {

    private final OrderId id;

    private final CustomerId customerId;

    private final EmailAddress customerEmail;

    private OrderStatus status;

    private final List<OrderItem> items;

    private final Money total;

    private final Instant placedAt;

    private Instant updatedAt;

    public Order(OrderId id,
                 CustomerId customerId,
                 EmailAddress customerEmail,
                 OrderStatus status,
                 List<OrderItem> items,
                 Money total,
                 Instant placedAt,
                 Instant updatedAt) {

        this.id = Objects.requireNonNull(id);

        this.customerId = Objects.requireNonNull(customerId);

        this.customerEmail = Objects.requireNonNull(customerEmail);

        this.status = Objects.requireNonNull(status);

        this.items = List.copyOf(Objects.requireNonNull(items));

        this.total = Objects.requireNonNull(total);

        this.placedAt = Objects.requireNonNull(placedAt);


        this.updatedAt = Objects.requireNonNull(updatedAt);

        validateAggregateInvariants();

    }

    public static Order place(OrderId orderId,
                              CustomerId customerId,
                              EmailAddress customerEmail,
                              List<OrderItemCandidate> candidates,
                              Instant now) {

        Objects.requireNonNull(candidates);

        Objects.requireNonNull(now);

        if (candidates.isEmpty()) {

            throw new IllegalArgumentException("Order must contain at least one item");

        }

        /*
         * Rule:
         * a same product cannot appear in two different lines.
         */

        Set<ProductId> productIds = new HashSet<>();

        for (OrderItemCandidate candidate : candidates) {

            boolean added = productIds.add(candidate.productId());

            if (!added) {

                throw new IllegalArgumentException("Duplicate product in order: " + candidate.productId().value());

            }

        }

        /*
         * Rule:
         * Order create your own items.
         */

        List<OrderItem> items = candidates
                .stream()
                .map(OrderItem::create)
                .toList();

        /*
         * Determinate the currency from the first item.
         */

        CurrencyCode currency = items.
                getFirst()
                .unitPrice()
                .value()
                .currency();

        /*
         * Cannot be possible to have multiples currencies in a single Order.
         */

        for (OrderItem item : items) {

            CurrencyCode itemCurrency = item
                    .unitPrice()
                    .value()
                    .currency();

            if (!currency.equals(itemCurrency)) {

                throw new IllegalArgumentException("All order items must use the same currency");

            }

        }

        /*
         * The domain itself calculates the total.
         */

        Money total = items
                .stream()
                .map(OrderItem::subtotal)
                .reduce(Money.zero(currency), Money::add);

        Order order = new Order(
                orderId,
                customerId,
                customerEmail,
                OrderStatus.PENDING,
                items,
                total,
                now,
                now
        );

        /*
         * A business event occurred:
         *
         * Order was placed.
         */

        order.registerDomainEvent(

                new OrderPlacedDomainEvent(
                        DomainEventId.newId(),
                        order.id,
                        order.customerId,
                        order.total,
                        order.items.size(),
                        now
                )

        );

        return order;

    }

    /*
     * Reconstitution from the database.
     *
     * Does NOT trigger an event.
     */

    public static Order restore(
            OrderId orderId,
            CustomerId customerId,
            EmailAddress customerEmail,
            OrderStatus status,
            List<OrderItem> items,
            Money persistedTotal,
            Instant placedAt,
            Instant updatedAt
    ) {

        Order order = new Order(
            orderId,
            customerId,
            customerEmail,
            status,
            items,
            persistedTotal,
            placedAt,
            updatedAt
        );

        /*
         * We also validate integrity
         * when rehydrating.
         */

        Money recalculated = order.calculateTotal();

        if (!recalculated.equals(persistedTotal)) {

            throw new IllegalStateException("Persisted order total does not match item total");

        }

        return order;

    }

    private void validateAggregateInvariants() {

        if (items.isEmpty()) {

            throw new IllegalArgumentException("Order must contain at least one item");

        }

        Set<ProductId> products = new HashSet<>();

        CurrencyCode currency = items
                .getFirst()
                .unitPrice()
                .value()
                .currency();

        for (OrderItem item : items) {

            if (!products.add(item.productId())) {

                throw new IllegalArgumentException("Order cannot contain duplicate products");

            }

            CurrencyCode itemCurrency = item
                    .unitPrice()
                    .value()
                    .currency();

            if (!currency.equals(itemCurrency)) {

                throw new IllegalArgumentException("Order cannot contain multiple currencies");

            }

        }

        if (!total.isPositive()) {

            throw new IllegalArgumentException("Order total must be positive");

        }

    }

    private Money calculateTotal() {

        CurrencyCode currency = items
                .getFirst()
                .unitPrice()
                .value()
                .currency();

        return items
                .stream()
                .map(OrderItem::subtotal)
                .reduce(
                        Money.zero(currency),
                        Money::add
                );

    }

    public OrderId id() {
        return id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public EmailAddress customerEmail() {
        return customerEmail;
    }

    public OrderStatus status() {
        return status;
    }

    public List<OrderItem> items() {
        return items;
    }

    public Money total() {
        return total;
    }

    public Instant placedAt() {
        return placedAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

}
