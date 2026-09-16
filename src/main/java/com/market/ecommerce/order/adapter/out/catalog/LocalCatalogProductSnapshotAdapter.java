package com.market.ecommerce.order.adapter.out.catalog;

import com.market.ecommerce.catalog.application.port.in.ProductCatalogUseCase;
import com.market.ecommerce.catalog.domain.Product;
import com.market.ecommerce.order.application.port.out.ProductSnapshotProvider;
import com.market.ecommerce.order.domain.OrderProductName;
import com.market.ecommerce.shared.domain.valueobjects.ProductId;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LocalCatalogProductSnapshotAdapter implements ProductSnapshotProvider {

    private final ProductCatalogUseCase catalog;

    public LocalCatalogProductSnapshotAdapter(ProductCatalogUseCase catalog) {
        this.catalog = catalog;
    }

    @Override
    public Map<ProductId, ProductSnapshot> findPurchasableProducts(Set<ProductId> productIds) {

        return catalog.findPurchasableProducts(productIds)
                .values()
                .stream()
                .collect(Collectors.toUnmodifiableMap(
                        Product::id,
                        product -> new ProductSnapshot(
                                product.id(),
                                new OrderProductName(product.name().value()),
                                product.price()
                        )
                ));

    }

}
