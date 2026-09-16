package com.market.ecommerce.order.domain;

import com.market.ecommerce.shared.domain.valueobjects.Money;
import com.market.ecommerce.shared.domain.valueobjects.ProductId;
import com.market.ecommerce.shared.domain.valueobjects.Quantity;
import com.market.ecommerce.shared.domain.valueobjects.UnitPrice;

import java.util.Objects;

public final class OrderItem {

    private final OrderItemId id;

    private final ProductId productId;

    private final OrderProductName productName;

    private final UnitPrice unitPrice;

    private final Quantity quantity;


    private OrderItem(OrderItemId id,
                      ProductId productId,
                      OrderProductName productName,
                      UnitPrice unitPrice,
                      Quantity quantity) {

        this.id = Objects.requireNonNull(id);

        this.productId = Objects.requireNonNull(productId);

        this.productName = Objects.requireNonNull(productName);

        this.unitPrice = Objects.requireNonNull(unitPrice);

        this.quantity = Objects.requireNonNull(quantity);

    }

    static OrderItem create(OrderItemCandidate candidate) {

        return new OrderItem(

                OrderItemId.newId(),

                candidate.productId(),

                candidate.productName(),

                candidate.unitPrice(),

                candidate.quantity()

        );

    }

    public static OrderItem restore(

        OrderItemId id,

        ProductId productId,

        OrderProductName productName,

        UnitPrice unitPrice,

        Quantity quantity

    ) {

        return new OrderItem(
                id,
                productId,
                productName,
                unitPrice,
                quantity
        );

    }

    public Money subtotal() {

        return unitPrice
                .value()
                .multiply(quantity);

    }

    public OrderItemId id() {
        return id;
    }

    public ProductId productId() {
        return productId;
    }

    public OrderProductName productName() {
        return productName;
    }

    public UnitPrice unitPrice() {
        return unitPrice;
    }

    public Quantity quantity() {
        return quantity;
    }

}
