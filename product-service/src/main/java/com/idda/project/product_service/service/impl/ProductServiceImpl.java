package com.idda.project.product_service.service.impl;

import com.idda.project.product_service.dto.request.DecreaseStockRequest;
import com.idda.project.product_service.dto.response.ProductResponse;
import com.idda.project.product_service.entity.Product;
import com.idda.project.product_service.repository.ProductRepository;
import com.idda.project.product_service.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<ProductResponse> getAllAvailableProducts() {
        List<Product> availableProducts = productRepository.findByStockGreaterThan(0);

        return availableProducts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        return convertToDTO(product);
    }

    @Override
    @Transactional
    public void decreaseProductStock(DecreaseStockRequest request) {
        int updatedRows = productRepository.decreaseStock(request.getProductId(), request.getQuantity());
        if (updatedRows == 0) {
            throw new RuntimeException("Failed to decrease stock, not enough items for product id: " + request.getProductId());
        }
    }

    private ProductResponse convertToDTO(Product product) {
        ProductResponse dto = new ProductResponse();
        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        return dto;
    }
}
