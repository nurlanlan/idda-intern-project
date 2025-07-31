package com.idda.project.card_service.service;

import com.idda.project.card_service.dto.request.AddCardRequest;
import com.idda.project.card_service.dto.request.DebitRequest;
import com.idda.project.card_service.dto.response.CardResponse;

import java.util.List;

public interface CardService {
    CardResponse addCard(AddCardRequest addCardRequest);

    List<CardResponse> getCardByUserId(Long userId);

    void deleteCard(Long cardId, Long userId);

    CardResponse getCardByIdAndUserId(Long cardId, Long userId);

    void debitCardBalance(DebitRequest request);

}
