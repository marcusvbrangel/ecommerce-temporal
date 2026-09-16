package com.market.ecommerce.order.adapter.out.persistence.jdbc;

import com.market.ecommerce.order.application.port.out.OrderRepository;
import com.market.ecommerce.order.domain.*;
import com.market.ecommerce.shared.domain.valueobjects.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcClient jdbcClient;

    public JdbcOrderRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(Order order) {

        insertOrder(order);

        insertItems(order);

    }

    private void insertOrder(Order order) {

        String sql = """
                insert into orders.orders
                (
                    id,
                    customer_id,
                    customer_email,
                    status,
                    total_amount,
                    total_currency,
                    placed_at,
                    updated_at
                )
                values
                (
                    :id,
                    :customer_id,
                    :customer_email,
                    :status,
                    :total_amount,
                    :total_currency,
                    :placed_at,
                    :updated_at
                )
                """;

        jdbcClient
                .sql(sql)
                .param("id", order.id().value())
                .param("customerId", order.customerId().value())
                .param("customerEmail", order.customerEmail().value())
                .param("status", order.status().name())
                .param("totalAmount", order.total().amount())
                .param("totalCurrency", order.total().currency().value())
                .param("placedAt", OffsetDateTime.ofInstant(order.placedAt(), ZoneOffset.UTC))
                .param("updatedAt", OffsetDateTime.ofInstant(order.updatedAt(), ZoneOffset.UTC))
                .update();

    }

    private void insertItems(Order order) {

        var sql = """
                insert int orders.orders_items
                (
                    id,
                    order_id,
                    product_id,
                    product_name,
                    unit_price_amount,
                    unit_price_currency,
                    quantity
                )
                values
                (
                    :id,
                    :order_id,
                    :product_id,
                    :product_name,
                    :unit_price_amount,
                    :unit_price_currency,
                    :quantity
                )
                """;

        for (OrderItem item : order.items()) {

            jdbcClient
                    .sql(sql)
                    .param("id", item.id().value())
                    .param("orderId", order.id().value())
                    .param("productId", item.productId().value())
                    .param("productName", item.productName().value())
                    .param("unitPriceAmount", item.unitPrice().value().amount())
                    .param("unitPriceCurrency", item.unitPrice().value().currency().value())
                    .param("quantity", item.quantity().value())
                    .update();

        }

    }

    @Override
    public Optional<Order> findById(OrderId orderId) {

        var sql = """
                SELECT
                    id,
                    customer_id,
                    customer_email,
                    status,
                    total_amount,
                    total_currency,
                    placed_at,
                    updated_at
                FROM orders
                WHERE id = :id
                """;

        Optional<OrderRow> row = jdbcClient

                        .sql(sql)
                        .param("id", orderId.value())
                        .query(
                                (resultSet, rowNum) ->
                                        new OrderRow(
                                                resultSet.getObject("id", UUID.class),
                                                resultSet.getObject("customer_id", UUID.class),
                                                resultSet.getString("customer_email"),
                                                resultSet.getString("status"),
                                                resultSet.getBigDecimal("total_amount"),
                                                resultSet.getString("total_currency"),
                                                resultSet.getObject("placed_at", OffsetDateTime.class).toInstant(),
                                                resultSet.getObject("updated_at", OffsetDateTime.class).toInstant()
                                        )
                        )

                        .optional();

        if (row.isEmpty()) {
            return Optional.empty();
        }

        List<OrderItem> items = findItems(orderId);

        OrderRow persisted = row.get();

        Order order = Order.restore(
                        OrderId.of(persisted.id()),
                        CustomerId.of(persisted.customerId()),
                        new EmailAddress(persisted.customerEmail()),
                        OrderStatus.valueOf(persisted.status()),
                        items,
                        Money.of(
                                persisted.totalAmount(),
                                CurrencyCode.of(persisted.totalCurrency())),
                        persisted.placedAt(),
                        persisted.updatedAt()
            );

        return Optional.of(order);

    }

    private List<OrderItem> findItems(OrderId orderId) {

        String sql = """
                SELECT
                    id,
                    product_id,
                    product_name,
                    unit_price_amount,
                    unit_price_currency,
                    quantity
                FROM order_items
                WHERE order_id = :orderId
                ORDER BY id
                """;

        return jdbcClient
                .sql(sql)
                .param("orderId", orderId.value())
                .query((resultSet, rowNum) ->
                        OrderItem.restore(
                                OrderItemId.of(resultSet.getObject("id", UUID.class)),
                                ProductId.of(resultSet.getObject("product_id", UUID.class)),
                                new OrderProductName(resultSet.getString("product_name")),
                                UnitPrice.of(
                                        Money.of(resultSet.getBigDecimal("unit_price_amount"),
                                                CurrencyCode.of(resultSet.getString("unit_price_currency")))
                                ),
                                Quantity.of(resultSet.getInt("quantity")))
                )
                .list();

    }

    private record OrderRow(

            UUID id,

            UUID customerId,

            String customerEmail,

            String status,

            java.math.BigDecimal totalAmount,

            String totalCurrency,

            java.time.Instant placedAt,

            java.time.Instant updatedAt

    ) {
    }

}
