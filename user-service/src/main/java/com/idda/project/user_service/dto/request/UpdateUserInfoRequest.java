package com.idda.project.user_service.dto.request;


import lombok.Data;

@Data
public class UpdateUserInfoRequest {
    private String fullName;
    private String address;
}
