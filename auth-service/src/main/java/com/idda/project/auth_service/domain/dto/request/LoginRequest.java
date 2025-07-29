package com.idda.project.auth_service.domain.dto.request;


import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
