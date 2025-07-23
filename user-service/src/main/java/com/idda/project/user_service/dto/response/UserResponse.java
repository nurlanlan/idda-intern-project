package com.idda.project.user_service.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private String fullName;
    private String email;
    private Long id;
    private boolean valid;
    private String message;
}
