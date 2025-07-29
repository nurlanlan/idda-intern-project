package com.idda.project.auth_service.service;

import com.idda.project.auth_service.domain.dto.request.LoginRequest;
import com.idda.project.auth_service.domain.dto.request.RegisterRequest;
import com.idda.project.auth_service.domain.dto.response.LoginResponse;


public interface AuthService {
    void register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
