package com.market.ecommerce.catalog.adapter.out.persistence.jdbc;

import com.market.ecommerce.catalog.application.port.out.ProductRepository;
import com.market.ecommerce.catalog.domain.Product;
import com.market.ecommerce.catalog.domain.ProductName;
import com.market.ecommerce.catalog.domain.ProductStatus;
import com.market.ecommerce.catalog.domain.Sku;
import com.market.ecommerce.shared.domain.valueobjects.CurrencyCode;
import com.market.ecommerce.shared.domain.valueobjects.Money;
import com.market.ecommerce.shared.domain.valueobjects.ProductId;
import com.market.ecommerce.shared.domain.valueobjects.UnitPrice;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JdbcProductRepository implements ProductRepository {

    private final JdbcClient jdbcClient;

    public JdbcProductRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Product> findAllActive() {

        String sql= """
                select
                    id,
                    sku,
                    name,
                    price_amount,
                    price_currency,
                    status,
                    created_at,
                    updated_at
                from catalog.catalog_products
                where status = 'ACTIVE'
                order by name
                """;



        List<Product> lista = jdbcClient
                .sql(sql)
                .query(rowMapper)
                .list();

        return lista;

    }

    @Override
    public Optional<Product> findById(ProductId productId) {

        String sql= """
                select
                    id,
                    sku,
                    name,
                    price_amount,
                    price_currency,
                    status,
                    created_at,
                    updated_at
                from catalog.catalog_products
                where id = :id
                """;

        return jdbcClient
                .sql(sql)
                .param("id", productId.value())
                .query(rowMapper)
                .optional();

    }

    @Override
    public Map<ProductId, Product> findActiveByIds(Set<ProductId> productIds) {

        if (productIds.isEmpty()) {
            return Map.of();
        }

        List<UUID> ids = productIds
                .stream()
                .map(ProductId::value)
                .toList();

        String sql= """
                select
                    id,
                    sku,
                    name,
                    price_amount,
                    price_currency,
                    status,
                    created_at,
                    updated_at
                from catalog.catalog_products
                where status = 'ACTIVE'
                and id in (:ids)
                """;

        return jdbcClient
                .sql(sql)
                .param("ids", ids)
                .query(rowMapper)
                .list()
                .stream()
                .collect(Collectors.toUnmodifiableMap(
                        Product::id,
                        product -> product
                ));

    }

    private final RowMapper<Product> rowMapper =

        (resultSet, rowNum) ->

            Product.restore(

                    ProductId.of(resultSet.getObject("id", UUID.class)),

                    new Sku(resultSet.getString("sku")),

                    new ProductName(resultSet.getString("name")),

                    UnitPrice.of(Money.of(resultSet.getBigDecimal("price_amount"),
                                 CurrencyCode.of(resultSet.getString("price_currency")))),

                    ProductStatus.valueOf(resultSet.getString("status")),

                    resultSet.getObject("created_at", OffsetDateTime.class).toInstant(),

                    resultSet.getObject("updated_at", OffsetDateTime.class).toInstant()
                    );

}
