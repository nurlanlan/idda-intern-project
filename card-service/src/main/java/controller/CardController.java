package controller;


import dto.request.AddCardRequest;
import dto.response.CardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CardService;

import java.util.List;


@RestController
@RequestMapping("/api/cards") // Bütün endpointlər üçün ümumi prefix
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


    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId, @RequestParam Long userId) {
        cardService.deleteCard(cardId, userId);
        return ResponseEntity.noContent().build();
    }
}