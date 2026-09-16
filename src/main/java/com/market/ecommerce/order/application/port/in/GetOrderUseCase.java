package com.market.ecommerce.order.application.port.in;

import com.market.ecommerce.order.domain.Order;
import com.market.ecommerce.order.domain.OrderId;

public interface GetOrderUseCase {

    Order get(OrderId orderId);

}
