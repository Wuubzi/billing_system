package com.billing_system.billing.DTO.Response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BillingResponse {
    private Long id_billing;
    private String billing_status;
    private Timestamp billing_created_at;
    private Double subtotal;
    private Integer quantity;
    private Long id_order;
    private String product_name;
    private Float product_sale_price;

    public BillingResponse(Long id_billing, String billing_status, Timestamp billing_created_at, Double subtotal, Integer quantity, Long id_order, String product_name, Float product_sale_price) {
        this.id_billing = id_billing;
        this.billing_status = billing_status;
        this.billing_created_at = billing_created_at;
        this.subtotal = subtotal;
        this.quantity = quantity;
        this.id_order = id_order;
        this.product_name = product_name;
        this.product_sale_price = product_sale_price;
    }
}
