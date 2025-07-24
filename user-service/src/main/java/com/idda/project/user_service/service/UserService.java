package com.idda.project.user_service.service;

import com.idda.project.user_service.dto.request.UpdateUserInfoRequest;
import com.idda.project.user_service.dto.response.UserResponse;

public interface UserService {
    UserResponse updateUserAddress(Long id, UpdateUserInfoRequest request);
    UserResponse updateUserFullName(Long id, UpdateUserInfoRequest request);
    UserResponse updateInfo(Long id, UpdateUserInfoRequest request);

    UserResponse getUserById(Long id);
}
