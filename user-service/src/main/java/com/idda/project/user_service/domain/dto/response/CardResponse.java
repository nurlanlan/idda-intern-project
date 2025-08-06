package com.idda.project.user_service.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
public class CardResponse {
    @Schema(description = "Unique identifier for the card", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
    private String maskedCardNumber;
    private YearMonth expirationDate;
    private BigDecimal balance;
    private boolean isActive;
}
