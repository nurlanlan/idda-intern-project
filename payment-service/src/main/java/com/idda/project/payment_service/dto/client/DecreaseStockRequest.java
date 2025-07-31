package com.idda.project.payment_service.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DecreaseStockRequest {
    private Long productId;
    private int quantity;
}
