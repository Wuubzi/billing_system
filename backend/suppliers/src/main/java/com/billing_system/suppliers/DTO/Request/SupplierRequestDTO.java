package com.billing_system.suppliers.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupplierRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "number is required")
    private String number;
    @NotBlank(message = "email is required")
    @Email(message = "Email is invalid")
    private String email;
}
