package com.market.ecommerce.order.application.port.in;

import com.market.ecommerce.order.domain.CustomerId;
import com.market.ecommerce.order.domain.EmailAddress;
import com.market.ecommerce.order.domain.Order;
import com.market.ecommerce.shared.domain.valueobjects.ProductId;
import com.market.ecommerce.shared.domain.valueobjects.Quantity;

import java.util.List;

public interface CreateOrderUseCase {

    Order create(Command command);

    record Command(
            CustomerId customerId,
            EmailAddress customerEmail,
            List<Item> items
    ) {}

    record Item(
            ProductId productId,
            Quantity quantity
    ) {}

}
