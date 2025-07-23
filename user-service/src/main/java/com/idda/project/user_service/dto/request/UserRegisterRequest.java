package com.idda.project.user_service.dto.request;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private int age;
    private String fullName;
    private String email;
    private String password;
}
