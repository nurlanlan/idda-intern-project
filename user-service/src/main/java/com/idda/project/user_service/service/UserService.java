package com.idda.project.user_service.service;

import com.idda.project.user_service.domain.dto.request.AddCardRequest;
import com.idda.project.user_service.domain.dto.request.UpdateUserInfoRequest;
import com.idda.project.user_service.domain.dto.response.CardResponse;
import com.idda.project.user_service.domain.dto.response.ProductResponse;
import com.idda.project.user_service.domain.dto.response.TransactionResponse;
import com.idda.project.user_service.domain.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    UserResponse updateInfo(Long id, UpdateUserInfoRequest request);

    UserResponse getUserById(Long id);

    CardResponse addCardForUser(Long userId, AddCardRequest request);

    List<CardResponse> getCardsForUser(Long userId);

    List<ProductResponse> getAllProducts();

    List<TransactionResponse> getTransactionHistory(Long userId);

    void deleteCard(Long userId, Long cardId);
}
