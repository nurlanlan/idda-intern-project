package com.idda.project.card_service.controller;


import com.idda.project.card_service.service.CardService;
import com.idda.project.card_service.dto.request.AddCardRequest;
import com.idda.project.card_service.dto.response.CardResponse;
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


    @PostMapping("/add")
    public ResponseEntity<CardResponse> addCard(@RequestBody AddCardRequest addCardRequest) {
        CardResponse newCard = cardService.addCard(addCardRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCard);
    }


    @GetMapping
    public ResponseEntity<List<CardResponse>> getCardsByUserId(@RequestParam Long userId) {
        List<CardResponse> userCards = cardService.getCardByUserId(userId);
        return ResponseEntity.ok(userCards);
    }


    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId, @RequestParam Long userId) {
        cardService.deleteCard(cardId, userId);
        return ResponseEntity.noContent().build();
    }
}