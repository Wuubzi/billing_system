package com.billing_system.orders.DTO.Request;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequestDTO {
    private List<OrderRequestDTO> products;
}
