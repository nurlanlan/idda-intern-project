package com.idda.project.payment_service.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DebitRequest {
    private Long cardId;
    private float amount;
}
