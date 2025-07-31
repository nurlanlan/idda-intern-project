package com.idda.project.user_service.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TransactionResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String maskedCardNumber;
    private int quantity;
    private float amount;
    private LocalDateTime transactionTimestamp;
}