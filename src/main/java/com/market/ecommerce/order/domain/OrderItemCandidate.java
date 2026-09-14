package com.market.ecommerce.order.domain;

import com.market.ecommerce.shared.domain.valueobjects.ProductId;
import com.market.ecommerce.shared.domain.valueobjects.Quantity;
import com.market.ecommerce.shared.domain.valueobjects.UnitPrice;

import java.util.Objects;

public record OrderItemCandidate(ProductId productId,
                                 OrderProductName productName,
                                 UnitPrice unitPrice,
                                 Quantity quantity) {

    public OrderItemCandidate {

        Objects.requireNonNull(productId);

        Objects.requireNonNull(productName);

        Objects.requireNonNull(unitPrice);

        Objects.requireNonNull(quantity);

    }

}
