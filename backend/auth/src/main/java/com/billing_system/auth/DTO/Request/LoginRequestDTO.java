package com.billing_system.auth.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDTO {
 @NotBlank(message = "Email cannot be empty")
 @Email(message = "Invalid email format")
 private String email;
 @NotBlank(message = "Password cannot be empty")
 @Size(min = 8, message = "Password must be at least 8 characters long")
 @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$",
         message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character")
 private String password;
}
