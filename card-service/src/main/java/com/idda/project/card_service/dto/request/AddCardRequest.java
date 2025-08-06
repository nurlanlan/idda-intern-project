package com.idda.project.card_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.math.BigDecimal;

@Data
public class AddCardRequest {

    private Long userId;

    @NotBlank(message = "Card number cannot be blank.")
    @Size(min = 16, max = 16, message = "Card number must contain exactly 16 characters.")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must contain only digits.")
    @Schema(description = "The 16-digit card number.", example = "4111222233334444", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cardNumber;

    @NotBlank(message = "CVV cannot be blank.")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "CVV must be 3 or 4 digits.")
    @Schema(description = "The 3 or 4-digit CVV code.", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cvv;

    @NotBlank(message = "Expiration date cannot be blank.")
    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/([0-9]{2})$", message = "Expiration date format must be MM/YY.")
    @Schema(description = "Card expiration date in MM/YY format.", example = "12/28", requiredMode = Schema.RequiredMode.REQUIRED)
    private String expirationDate;

    @NotNull(message = "Balance is required, can be 0.")
    @Schema(description = "Initial balance of the card.", example = "1500.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal balance;


}
