package com.idda.project.payment_service.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CardDTO {
    private Long id;
    private String maskedCardNumber;
    private boolean isActive;
    private float balance;
}
