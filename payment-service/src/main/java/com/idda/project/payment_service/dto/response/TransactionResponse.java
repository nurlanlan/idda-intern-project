package com.idda.project.payment_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
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
    private BigDecimal amount;
    private LocalDateTime transactionTimestamp;
}
