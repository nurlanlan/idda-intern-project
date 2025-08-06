package com.idda.project.payment_service.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class CardDTO {
    private Long id;
    private String maskedCardNumber;
    private boolean isActive;
    private BigDecimal balance;
}
