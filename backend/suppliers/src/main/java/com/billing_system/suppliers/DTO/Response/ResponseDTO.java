package com.billing_system.suppliers.DTO.Response;

import lombok.Data;

@Data
public class ResponseDTO {
    private String code;
    private String message;
    private String uri;
    private String timestamp;
    private Object data;

}
