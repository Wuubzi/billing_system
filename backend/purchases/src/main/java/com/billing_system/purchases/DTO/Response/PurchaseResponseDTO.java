package com.billing_system.purchases.DTO.Response;

import lombok.Data;

@Data
public class PurchaseResponseDTO {
    private Long id_purchase;
    private String name_product;
    private String name_supplier;
    private Integer quantity;
    private String expiration_date;
    private String status;

    public PurchaseResponseDTO(Long id_purchase, String name_product, String name_supplier, Integer quantity, String expiration_date, String status) {
        this.id_purchase = id_purchase;
        this.name_product = name_product;
        this.name_supplier = name_supplier;
        this.quantity = quantity;
        this.expiration_date = expiration_date;
        this.status = status;
    }
}
