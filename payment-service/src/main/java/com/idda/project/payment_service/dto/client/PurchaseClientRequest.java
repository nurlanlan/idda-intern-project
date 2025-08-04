package com.idda.project.payment_service.dto.client;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PurchaseClientRequest {

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @NotNull(message = "Card ID cannot be null")
    private Long cardId;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be a positive number")
    private int quantity;
}