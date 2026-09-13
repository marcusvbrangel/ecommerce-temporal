package com.market.ecommerce.catalog.application.port.in;

import com.market.ecommerce.catalog.domain.Product;
import com.market.ecommerce.shared.domain.valueobjects.ProductId;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ProductCatalogUseCase {

    List<Product> listActiveProducts();

    Optional<Product> findProduct(ProductId productId);

    Map<ProductId, Product> findPurchasableProducts(Set<ProductId> productIds);

}
