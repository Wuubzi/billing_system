package com.billing_system.orders.DTO.Request;

import lombok.Data;

@Data
public class OrderRequestDTO {
    private Long id_product;
    private int quantity;
}
