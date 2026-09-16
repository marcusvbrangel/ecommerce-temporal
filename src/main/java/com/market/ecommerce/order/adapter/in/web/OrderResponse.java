package com.market.ecommerce.order.adapter.in.web;

import com.market.ecommerce.order.domain.Order;
import com.market.ecommerce.order.domain.OrderItem;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(

        UUID orderId,

        UUID customerId,

        String customerEmail,

        String status,

        List<Item> items,

        BigDecimal totalAmount,

        String currency,

        Instant placedAt
) {

    public static OrderResponse from(Order order) {

        return new OrderResponse(

                order.id().value(),

                order.customerId().value(),

                order.customerEmail().value(),

                order.status().name(),

                order.items().stream().map(Item::from).toList(),

                order.total().amount(),

                order.total().currency().value(),

                order.placedAt()
        );

    }

    public record Item(

            UUID productId,

            String productName,

            BigDecimal unitPrice,

            int quantity,

            BigDecimal subtotal

    ) {

        static Item from(OrderItem item) {

            return new Item(

                    item.productId().value(),

                    item.productName().value(),

                    item.unitPrice().value().amount(),

                    item.quantity().value(),

                    item.subtotal().amount()

            );

        }

    }

}
