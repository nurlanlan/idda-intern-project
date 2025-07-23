package com.idda.project.user_service.service;

import com.idda.project.user_service.dto.request.UserLoginRequest;
import com.idda.project.user_service.dto.request.UserRegisterRequest;
import com.idda.project.user_service.dto.response.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRegisterRequest request);
    UserResponse loginUser(UserLoginRequest request);
}
