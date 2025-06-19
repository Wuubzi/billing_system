package com.billing_system.products.DTO.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LowStockProducts {
    private String product_name;
    private String product_image;
    private Integer remaining_stock;

    public LowStockProducts(String product_name, String product_image, Integer remaining_stock) {
        this.product_name = product_name;
        this.product_image = product_image;
        this.remaining_stock = remaining_stock;
    }
}
