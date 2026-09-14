package com.market.ecommerce.catalog.adapter.in.web;

import com.market.ecommerce.catalog.domain.Product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(UUID id,
                              String sku,
                              String name,
                              BigDecimal price,
                              String currency) {

    public static ProductResponse from(Product product) {

        return new ProductResponse(
                product.id().value(),
                product.sku().value(),
                product.name().value(),
                product.price().value().amount(),
                product.price().value().currency().value()
        );

    }

}
