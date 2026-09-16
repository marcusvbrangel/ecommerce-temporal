package com.market.ecommerce.order.application.service;

import com.market.ecommerce.order.application.port.in.GetOrderUseCase;
import com.market.ecommerce.order.application.port.out.OrderRepository;
import com.market.ecommerce.order.domain.Order;
import com.market.ecommerce.order.domain.OrderId;
import org.springframework.transaction.annotation.Transactional;

public class GetOrderService implements GetOrderUseCase {

    private final OrderRepository orderRepository;

    public GetOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional(readOnly = false)
    public Order get(OrderId orderId) {

        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId.value()));

    }

}
