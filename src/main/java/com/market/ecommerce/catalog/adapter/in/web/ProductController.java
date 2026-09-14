package com.market.ecommerce.catalog.adapter.in.web;

import com.market.ecommerce.catalog.application.port.in.ProductCatalogUseCase;
import com.market.ecommerce.shared.domain.valueobjects.ProductId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductCatalogUseCase productCatalogUseCase;

    public ProductController(ProductCatalogUseCase productCatalogUseCase) {
        this.productCatalogUseCase = productCatalogUseCase;
    }

    @GetMapping
    public List<ProductResponse> list() {

        return productCatalogUseCase.listActiveProducts()
                .stream()
                .map(ProductResponse::from)
                .toList();

    }

    @GetMapping("/productId")
    public ResponseEntity<ProductResponse> find(@PathVariable UUID productId) {

        return productCatalogUseCase.findProduct(ProductId.of(productId))
                .map(ProductResponse::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());

    }


























}
