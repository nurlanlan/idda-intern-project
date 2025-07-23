package com.idda.project.user_service.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private int age;
    private String address;
}
