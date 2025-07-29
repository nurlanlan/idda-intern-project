package com.idda.project.user_service.domain.dto.request;


import lombok.Data;

@Data
public class UpdateUserInfoRequest {
    private String address;
    private Boolean gender;
}
