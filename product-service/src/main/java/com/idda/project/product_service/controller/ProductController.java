package com.idda.project.product_service.controller;

import com.idda.project.product_service.dto.response.ProductResponse;
import com.idda.project.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/available")
    public ResponseEntity<List<ProductResponse>> getAllAvailableProducts() {
        List<ProductResponse> products = productService.getAllAvailableProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable long productId) {
        ProductResponse product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

}
