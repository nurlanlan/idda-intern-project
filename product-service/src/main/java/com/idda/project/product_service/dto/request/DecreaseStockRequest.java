package com.idda.project.product_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DecreaseStockRequest {
    @NotNull
    private Long productId;

    @NotNull
    @Positive(message = "Quantity must be a positive number")
    private int quantity;
}
