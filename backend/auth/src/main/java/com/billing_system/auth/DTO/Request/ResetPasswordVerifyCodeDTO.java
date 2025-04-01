package com.billing_system.auth.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordVerifyCodeDTO {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Verification code cannot be empty")
    @Size(min = 6, max = 6, message = "Verification code must be exactly 6 digits long")
    @Pattern(regexp = "^[0-9]{6}$", message = "Verification code must contain only digits")
    private String code;

}
