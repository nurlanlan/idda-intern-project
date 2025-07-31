package com.idda.project.payment_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PurchaseResponseDTO {
    private Long transactionId;
    private String status;
    private String message;
    private LocalDateTime timestamp;
}
