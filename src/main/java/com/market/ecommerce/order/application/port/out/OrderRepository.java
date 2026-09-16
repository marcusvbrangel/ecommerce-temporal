package com.market.ecommerce.order.application.port.out;

import com.market.ecommerce.order.domain.Order;
import com.market.ecommerce.order.domain.OrderId;

import java.util.Optional;

public interface OrderRepository {

    void save(Order order);

    Optional<Order> findById(OrderId orderId);

}
