package com.idda.project.auth_service.domain.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private int age;
    private String fullName;
    private String email;
    private String password;
}
