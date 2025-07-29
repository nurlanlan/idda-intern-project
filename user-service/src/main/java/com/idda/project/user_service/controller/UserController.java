package com.idda.project.user_service.controller;

import com.idda.project.user_service.domain.dto.request.AddCardRequest;
import com.idda.project.user_service.domain.dto.request.UpdateUserInfoRequest;
import com.idda.project.user_service.domain.dto.response.CardResponse;
import com.idda.project.user_service.domain.dto.response.UserResponse;
import com.idda.project.user_service.domain.entity.User;
import com.idda.project.user_service.service.UserService;
import com.idda.project.user_service.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

//    @PutMapping("/{id}/full-name")
//    public ResponseEntity<UserResponse> updateUserFullName(
//            @PathVariable Long id,
//            @RequestBody UpdateUserInfoRequest request
//    ) {
//        return ResponseEntity.ok(userService.updateUserFullName(id, request));
//    }
//
//    @PutMapping("/{id}/address")
//    public ResponseEntity<UserResponse> updateUserAddress(
//            @PathVariable Long id,
//            @RequestBody UpdateUserInfoRequest request
//    ) {
//        return ResponseEntity.ok(userService.updateUserAddress(id, request));
//    }

    @PatchMapping("/{id}/info")
    public ResponseEntity<UserResponse> updateUserInfo(
            @PathVariable Long id,
            @RequestBody UpdateUserInfoRequest request
    ) {
        return ResponseEntity.ok(userService.updateInfo(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

//    @PostMapping("/me/cards")
//    public ResponseEntity<CardResponse> addCard(@Valid @RequestBody AddCardRequest request, Principal principal) {
//        Long userId = getUserIdFromPrincipal(principal); // Bu, sizin təhlükəsizlik məntiqinizdən asılıdır
//
//        CardResponse createdCard = userService.addCardForUser(userId, request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
//    }

    @PostMapping("/{userId}/cards")   // Burada userId-ni URL-də alırıq -TEST ucun
    public ResponseEntity<CardResponse> addCardForUser(
            @PathVariable Long userId,
            @Valid @RequestBody AddCardRequest request) {

        CardResponse createdCard = userService.addCardForUser(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }

//    @GetMapping("/me/cards")
//    public ResponseEntity<List<CardResponse>> getCards(Principal principal) {
//        Long userId = getUserIdFromPrincipal(principal);
//
//        List<CardResponse> cards = userService.getCardsForUser(userId);
//        return ResponseEntity.ok(cards);
//    }

    @GetMapping("/{userId}/cards")   // Burada userId-ni URL-də alırıq -TEST ucun
    public ResponseEntity<List<CardResponse>> getCardsForUser(@PathVariable Long userId) {

        List<CardResponse> cards = userService.getCardsForUser(userId);
        return ResponseEntity.ok(cards);
    }

//    private Long getUserIdFromPrincipal(Principal principal) {
//        User userDetails = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
//        return userDetails.getId();
//    }

}
