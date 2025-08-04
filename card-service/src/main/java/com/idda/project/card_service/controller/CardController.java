package com.idda.project.card_service.controller;

// ... importlar ...

import com.idda.project.card_service.dto.request.AddCardRequest;
import com.idda.project.card_service.dto.request.DebitRequest;
import com.idda.project.card_service.dto.response.CardResponse;
import com.idda.project.card_service.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardResponse> addCard(@RequestBody AddCardRequest addCardRequest) {
        CardResponse newCard = cardService.addCard(addCardRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCard);
    }

    @GetMapping
    public ResponseEntity<List<CardResponse>> getCardsByUserId(@RequestParam Long userId) {
        List<CardResponse> userCards = cardService.getCardByUserId(userId);
        return ResponseEntity.ok(userCards);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponse> getCardDetails(
            @PathVariable Long cardId,
            @RequestParam Long userId) {
        CardResponse card = cardService.getCardByIdAndUserId(cardId, userId);
        return ResponseEntity.ok(card);
    }

    @PostMapping("/debit")
    public ResponseEntity<Void> debit(@Valid @RequestBody DebitRequest request) {
        cardService.debitCardBalance(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(
            @PathVariable Long cardId,
            @RequestParam Long userId) {
        cardService.deleteCard(cardId, userId);
        return ResponseEntity.noContent().build();
    }
}