package com.idda.project.auth_service.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Valid
public class RegisterRequest {
    @NotNull(message = "Age cannot be null")
    @Min(value = 18, message = "Age must be at least 18")
    @Schema(description = "User's age, must be 18 or older.", example = "25", requiredMode = Schema.RequiredMode.REQUIRED)
    private int age;

    @NotBlank(message = "Full name cannot be blank")
    @Schema(description = "User's full name.", example = "Ali Valiyev", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fullName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be in a valid format")
    @Schema(description = "User's unique email address.", example = "ali.valiyev@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "Password must contain at least one letter and one number.")
    @Schema(description = "User's password (min 8 characters, with letters and numbers).", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
