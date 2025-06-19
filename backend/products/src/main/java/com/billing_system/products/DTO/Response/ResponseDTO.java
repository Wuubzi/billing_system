package com.billing_system.products.DTO.Response;

import lombok.Data;

@Data
public class ResponseDTO {
    private String code;
    private String message;
    private String uri;
    private String timestamp;
}
