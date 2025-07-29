package com.idda.project.user_service.service;

import com.idda.project.user_service.domain.dto.request.AddCardRequest;
import com.idda.project.user_service.domain.dto.request.UpdateUserInfoRequest;
import com.idda.project.user_service.domain.dto.response.CardResponse;
import com.idda.project.user_service.domain.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    UserResponse updateInfo(Long id, UpdateUserInfoRequest request);

    UserResponse getUserById(Long id);

    CardResponse addCardForUser(Long userId, AddCardRequest request);

    List<CardResponse> getCardsForUser(Long userId);


}
