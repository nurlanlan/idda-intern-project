package com.idda.project.user_service.domain.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCardToCardServiceRequest {
    private Long userId;
    private String cardNumber;
    private String cvv;
    private String expirationDate;
    private BigDecimal balance;
}
