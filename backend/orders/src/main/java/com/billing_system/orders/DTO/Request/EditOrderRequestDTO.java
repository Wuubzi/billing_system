package com.billing_system.orders.DTO.Request;

import lombok.Data;

import java.util.List;

@Data
public class EditOrderRequestDTO {
    private String status;
    private List<OrderRequestDTO> products;
}
