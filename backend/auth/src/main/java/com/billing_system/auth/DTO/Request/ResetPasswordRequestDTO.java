package com.billing_system.auth.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;
}
