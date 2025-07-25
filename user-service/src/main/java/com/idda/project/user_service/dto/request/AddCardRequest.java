package com.idda.project.user_service.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class AddCardRequest {

    private Long userId;

    @NotBlank(message = "Card number cannot be blank.")
    @Size(min = 16, max = 16, message = "Card number must contain exactly 16 characters.")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must contain only digits.")
    private String cardNumber;

    @NotBlank(message = "CVV cannot be blank.")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "CVV must be 3 or 4 digits.")
    private String cvv;

    @NotBlank(message = "Expiration date cannot be blank.")
    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/([0-9]{2})$", message = "Expiration date format must be MM/YY.")
    private String expirationDate;

    @PositiveOrZero(message = "Balance must be zero or a positive value.")
    private float balance = 0.0f;

}