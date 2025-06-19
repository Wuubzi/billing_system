package com.billing_system.orders.Interfaces;

public interface OrderProductInterface {
    Long getId_order();
    Object getSubtotal();
    String getStatus();
    Integer getQuantity();
    String getProduct_name();
    Double getProduct_price();
}
