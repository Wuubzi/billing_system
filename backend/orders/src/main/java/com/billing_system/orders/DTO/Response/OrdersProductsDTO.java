package com.billing_system.orders.DTO.Response;

import lombok.Data;

@Data
public class OrdersProductsDTO {
    private int quantity;
    private String product_name;
    private double product_price;

    public OrdersProductsDTO(int quantity, String product_name, double product_price) {
        this.quantity = quantity;
        this.product_name = product_name;
        this.product_price = product_price;
    }
}
