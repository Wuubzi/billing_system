package com.billing_system.purchases.DTO.Request;

import lombok.Data;

import java.util.Date;

@Data
public class PurchaseEditRequestDTO {
    private Long id_product;
    private Integer quantity;
    private Date expiration_date;
    private String status;
}
