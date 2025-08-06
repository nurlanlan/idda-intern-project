package com.idda.project.user_service.domain.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateUserInfoRequest {

    @Schema(description = "User's new residential address.", example = "Baku, Fuzuli Street 45")
    private String address;

    @Schema(description = "User's gender. (true for male, false for female, or based on convention)", example = "true")
    private Boolean gender;
}
