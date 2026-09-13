package com.market.ecommerce.catalog.application.port.out;

import com.market.ecommerce.catalog.domain.Product;
import com.market.ecommerce.shared.domain.valueobjects.ProductId;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ProductRepository {

    List<Product> findAllActive();

    Optional<Product> findById(ProductId productId);

    Map<ProductId, Product> findActiveByIds(Set<ProductId> productIds);

}
