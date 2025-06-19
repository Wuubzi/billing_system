package com.billing_system.products.DTO.Response;

import jakarta.websocket.server.ServerEndpoint;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BestSellingProducts {
    private String product_name;
    private Long total_sold_quantity;
    private Integer current_stock;
    private BigDecimal product_price;

    public BestSellingProducts(String product_name, Long total_sold_quantity, Integer current_stock, BigDecimal product_price) {
        this.product_name = product_name;
        this.total_sold_quantity = total_sold_quantity;
        this.current_stock = current_stock;
        this.product_price = product_price;
    }
}
