package com.idda.project.payment_service.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DebitRequest {
    private Long cardId;
    private BigDecimal amount;
}
