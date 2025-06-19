package com.billing_system.billing.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class InvoiceItem {
    private String name;
    private Integer quantity;
    private Double price;
    private Double total;


    public InvoiceItem(String productName, Integer quantity, Float productSalePrice) {
        this.name = productName;
        this.quantity = quantity;
        this.price = productSalePrice.doubleValue();
        this.total = this.price * quantity;
    }
}
