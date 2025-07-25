package com.idda.project.card_service.dto.request;

import lombok.Data;

@Data
public class AddCardRequest {

    private Long userId;
    private String cardNumber;
    private String cvv;
    private String expirationDate;
    private float balance;


}
