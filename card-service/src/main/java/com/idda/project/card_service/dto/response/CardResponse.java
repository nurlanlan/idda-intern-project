package com.idda.project.card_service.dto.response;

import lombok.Data;

import java.time.YearMonth;

@Data
public class CardResponse {
    private Long id;
    private String maskedCardNumber;
    private YearMonth expirationDate;
    private float balance;
    private boolean isActive;

}
