package com.idda.project.payment_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseRequestDTO {
    @NotNull
    private Long userId;
    @NotNull
    private Long productId;
    @NotNull
    private Long cardId;
    @NotNull
    private int quantity;

    public PurchaseRequestDTO() {

    }
}
