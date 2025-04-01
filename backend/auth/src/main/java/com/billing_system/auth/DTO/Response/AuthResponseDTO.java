package com.billing_system.auth.DTO.Response;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String timestamp;
    private String code;
    private String message;
    private String token;
    private String uri;
}
