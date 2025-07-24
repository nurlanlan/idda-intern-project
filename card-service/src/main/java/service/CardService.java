package service;

import dto.request.AddCardRequest;
import dto.response.CardResponse;

import java.util.List;
import java.util.Optional;

public interface CardService {
    CardResponse addCard(AddCardRequest addCardRequest);

    List<CardResponse> getCardByUserId(Long userId);

    void deleteCard(Long cardId, Long userId);

}
