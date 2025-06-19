package com.billing_system.users.DTO.Request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequestDTO {
    @NotBlank(message = "The name cannot be empty")
    @Size(min = 2, max = 50, message = "The name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "The last name cannot be empty")
    @Size(min = 2, max = 50, message = "The last name must be between 2 and 50 characters")
    private String last_name;

    @NotBlank(message = "The email cannot be empty")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "The email cannot exceed 100 characters")
    private String email;

    @NotBlank(message = "The phone number cannot be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "The phone number must contain exactly 10 digits")
    private String phoneNumber;

    @NotBlank(message = "The address cannot be empty")
    @Size(max = 255, message = "The address cannot exceed 255 characters")
    private String address;

    @Size(max = 255, message = "The avatar cannot exceed 255 characters")
    private String avatar;
}
