package com.idda.project.card_service.service.impl;

import com.idda.project.card_service.dto.request.AddCardRequest;
import com.idda.project.card_service.dto.response.CardResponse;
import com.idda.project.card_service.entity.Card;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.idda.project.card_service.repository.CardRepository;
import com.idda.project.card_service.service.CardService;
import com.idda.project.card_service.service.EncryptionService;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final EncryptionService encryptionService;

    @Override
    @Transactional
    public CardResponse addCard(AddCardRequest addCardRequest) {
        String encryptedCardNumber = encryptionService.encrypt(addCardRequest.getCardNumber());
        cardRepository.findByEncryptedCardNumber(encryptedCardNumber).ifPresent(card -> {
            throw new RuntimeException("Card already exists");
        });
        Card card = new Card();
        card.setUserId(addCardRequest.getUserId());
        card.setEncryptedCardNumber(encryptedCardNumber);
        card.setBalance(addCardRequest.getBalance());
        card.setEncryptedCvv(encryptionService.encrypt(addCardRequest.getCvv()));
        card.setActive(true);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth expiration = YearMonth.parse(addCardRequest.getExpirationDate(), formatter);
        card.setExpirationDate(expiration);

        Card savedCard = cardRepository.save(card);

        return convertToResponseDTO(savedCard);
    }

    @Override
    @Transactional
    public List<CardResponse> getCardByUserId(Long userId) {
        List<Card> userCards = cardRepository.findByUserId(userId);
        return userCards.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCard(Long cardId, Long userId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        if (!card.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized action");
        }

        cardRepository.delete(card);

    }

    private CardResponse convertToResponseDTO(Card card) {
        CardResponse dto = new CardResponse();
        dto.setId(card.getId());
        dto.setMaskedCardNumber(maskCardNumber(encryptionService.decrypt(card.getEncryptedCardNumber())));
        dto.setExpirationDate(card.getExpirationDate());
        dto.setBalance(card.getBalance());
        dto.setActive(card.isActive());
        return dto;
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        int visibleCount = 4;
        String lastDigits = cardNumber.substring(cardNumber.length() - visibleCount);
        return "************" + lastDigits;
    }


}

