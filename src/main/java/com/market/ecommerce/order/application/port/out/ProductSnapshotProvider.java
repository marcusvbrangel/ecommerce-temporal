package com.market.ecommerce.order.application.port.out;

import com.market.ecommerce.order.domain.OrderProductName;
import com.market.ecommerce.shared.domain.valueobjects.ProductId;
import com.market.ecommerce.shared.domain.valueobjects.UnitPrice;

import java.util.Map;
import java.util.Set;

public interface ProductSnapshotProvider {

    Map<ProductId, ProductSnapshot> findPurchasableProducts(Set<ProductId> productIds);

    record ProductSnapshot(
            ProductId productId,
            OrderProductName name,
            UnitPrice unitPrice
    ) {}

}
