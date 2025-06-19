package com.billing_system.auth.DTO.Response;

import lombok.Data;

@Data
public class ResponseDTO {
    private String code;
    private String message;
    private String identifier;
    private String uri;
    private String Timestamp;
}
