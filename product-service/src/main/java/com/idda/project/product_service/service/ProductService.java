package com.idda.project.product_service.service;

import com.idda.project.product_service.dto.request.DecreaseStockRequest;
import com.idda.project.product_service.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllAvailableProducts();

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(long productId);

    void decreaseProductStock(DecreaseStockRequest request);


}
