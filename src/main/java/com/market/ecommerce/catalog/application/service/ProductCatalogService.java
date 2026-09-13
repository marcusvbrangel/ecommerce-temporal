package com.market.ecommerce.catalog.application.service;

import com.market.ecommerce.catalog.application.port.in.ProductCatalogUseCase;
import com.market.ecommerce.catalog.application.port.out.ProductRepository;
import com.market.ecommerce.catalog.domain.Product;
import com.market.ecommerce.shared.domain.valueobjects.ProductId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductCatalogService implements ProductCatalogUseCase {

    private final ProductRepository productRepository;

    public ProductCatalogService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> listActiveProducts() {
        return productRepository.findAllActive();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findProduct(ProductId productId) {
        return productRepository.findById(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<ProductId, Product> findPurchasableProducts(Set<ProductId> productIds) {
        return productRepository.findActiveByIds(productIds);
    }

}
