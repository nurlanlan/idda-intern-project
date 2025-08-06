package com.idda.project.card_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DebitRequest {
    @NotNull
    private Long cardId;

    @NotNull
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
}
