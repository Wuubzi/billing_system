package com.billing_system.orders.DTO.Response;

import lombok.Data;

import java.util.List;

@Data
public class OrdersResponseDTO {
    private Long id_order;
    private Object subtotal;
    private String status;
    List<OrdersProductsDTO> ordersProducts;

}
