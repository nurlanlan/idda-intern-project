package com.idda.project.user_service.service.impl;

import com.idda.project.user_service.domain.dto.request.AddCardRequest;
import com.idda.project.user_service.domain.dto.request.UpdateUserInfoRequest;
import com.idda.project.user_service.domain.dto.response.CardResponse;
import com.idda.project.user_service.domain.dto.response.ProductResponse;
import com.idda.project.user_service.domain.dto.response.TransactionResponse;
import com.idda.project.user_service.domain.dto.response.UserResponse;
import com.idda.project.user_service.domain.entity.User;
import com.idda.project.user_service.repository.UserRepository;
import com.idda.project.user_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WebClient.Builder webClientBuilder;


    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user);
    }

    @Override
    public CardResponse addCardForUser(Long userId, AddCardRequest request) {
        AddCardRequest requestForCardService = new AddCardRequest();
        requestForCardService.setUserId(userId);
        requestForCardService.setCardNumber(request.getCardNumber());
        requestForCardService.setCvv(request.getCvv());
        requestForCardService.setExpirationDate(request.getExpirationDate());
        requestForCardService.setBalance(request.getBalance());

        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8083/api/cards")
                .bodyValue(requestForCardService)
                .retrieve()
                .bodyToMono(CardResponse.class)
                .block();
    }

    @Override
    public List<CardResponse> getCardsForUser(Long userId) {
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8083/api/cards", uriBuilder ->
                        uriBuilder.queryParam("userId", userId).build())
                .retrieve()
                .bodyToFlux(CardResponse.class)
                .collectList()
                .block();
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8084/api/products/available")
                .retrieve()
                .bodyToFlux(ProductResponse.class)
                .collectList()
                .block();
    }

    @Override
    public List<TransactionResponse> getTransactionHistory(Long userId) {
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8085/api/payments/history?userId={userId}", userId)
                .retrieve()
                .bodyToFlux(TransactionResponse.class)
                .collectList()
                .block();
    }

    @Override
    public void deleteCard(Long userId, Long cardId) {
        webClientBuilder.build()
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(8083)
                        .path("/api/cards/{cardId}") // PathVariable-ı burada təyin edirik
                        .queryParam("userId", userId) // QueryParam-ı isə burada
                        .build(cardId)) // .build() metodu {cardId} yerinə dəyəri qoyur
                .retrieve()
                .toBodilessEntity()
                .block(); // block() ResponseEntity<Void> qaytarır, onu return etməyə ehtiyac yoxdur.
    }



    public UserResponse updateInfo(Long id, UpdateUserInfoRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }

        User updatedUser = userRepository.save(user);
        return mapToResponse(updatedUser);
    }

    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setGender(user.getGender());
        response.setAddress(user.getAddress());
        return response;
    }

}


