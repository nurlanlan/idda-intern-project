package com.idda.project.user_service.controller;

import com.idda.project.user_service.domain.dto.request.AddCardRequest;
import com.idda.project.user_service.domain.dto.request.UpdateUserInfoRequest;
import com.idda.project.user_service.domain.dto.response.CardResponse;
import com.idda.project.user_service.domain.dto.response.ProductResponse;
import com.idda.project.user_service.domain.dto.response.TransactionResponse;
import com.idda.project.user_service.domain.dto.response.UserResponse;
import com.idda.project.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/me/profile")
    public ResponseEntity<UserResponse> getMyProfile(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PatchMapping("/me/profile")
    public ResponseEntity<UserResponse> updateMyProfile(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid UpdateUserInfoRequest request
    ) {
        return ResponseEntity.ok(userService.updateInfo(userId, request));
    }


    @PostMapping("/me/cards")
    public ResponseEntity<CardResponse> addMyCard(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody AddCardRequest request) {

        CardResponse createdCard = userService.addCardForUser(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }

    @DeleteMapping("/me/cards/{cardId}")
    public ResponseEntity<Void> deleteMyCard(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long cardId) {
        userService.deleteCard(userId, cardId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/cards")
    public ResponseEntity<List<CardResponse>> getMyCards(@RequestHeader("X-User-Id") Long userId) {
        List<CardResponse> cards = userService.getCardsForUser(userId);
        return ResponseEntity.ok(cards);
    }


    @GetMapping("/me/transactions")
    public ResponseEntity<List<TransactionResponse>> getMyTransactionHistory(@RequestHeader("X-User-Id") Long userId) {
        List<TransactionResponse> transactions = userService.getTransactionHistory(userId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = userService.getAllProducts();
        return ResponseEntity.ok(products);
    }
}