package com.idda.project.payment_service.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String maskedCardNumber;
    private int quantity;
    private float amount;
    private LocalDateTime transactionTimestamp;
}
