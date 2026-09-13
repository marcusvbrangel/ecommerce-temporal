package com.market.ecommerce.catalog.domain;

import com.market.ecommerce.shared.domain.valueobjects.ProductId;
import com.market.ecommerce.shared.domain.valueobjects.UnitPrice;

import java.time.Instant;
import java.util.Objects;

public class Product {

    private final ProductId id;
    private final Sku sku;
    private final ProductName name;
    private final UnitPrice price;
    private final ProductStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Product(ProductId id,
                   Sku sku,
                   ProductName name,
                   UnitPrice price,
                   ProductStatus status,
                   Instant createdAt,
                   Instant updatedAt) {

        this.id = Objects.requireNonNull(id);
        this.sku = Objects.requireNonNull(sku);
        this.name = Objects.requireNonNull(name);
        this.price = Objects.requireNonNull(price);
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);

    }

    public static Product restore(
                   ProductId id,
                   Sku sku,
                   ProductName name,
                   UnitPrice price,
                   ProductStatus status,
                   Instant createdAt,
                   Instant updatedAt) {

        return new Product(
          id,
          sku,
          name,
          price,
          status,
          createdAt,
          updatedAt
        );

    }

    public boolean isPurchasable() {
        return status == ProductStatus.ACTIVE;
    }

    public ProductId id() {
        return id;
    }

    public Sku sku() {
        return sku;
    }

    public ProductName name() {
        return name;
    }

    public UnitPrice price() {
        return price;
    }

    public ProductStatus status() {
        return status;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
