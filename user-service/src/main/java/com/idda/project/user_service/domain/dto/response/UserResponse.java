package com.idda.project.user_service.domain.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String fullName;
    private Boolean gender;
    private String address;
}
