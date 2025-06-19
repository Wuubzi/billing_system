package com.billing_system.users.DTO.Response;

import lombok.Data;

@Data
public class ExceptionResponseDTO {
    private String code;
    private String Exception;
    private String message;
    private String uri;
    private String timestamp;
}
