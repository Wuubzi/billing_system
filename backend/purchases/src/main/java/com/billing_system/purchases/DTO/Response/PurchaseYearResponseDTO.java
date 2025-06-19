package com.billing_system.purchases.DTO.Response;

import lombok.Data;

@Data
public class PurchaseYearResponseDTO {
    private String month;
    private Long purchase;

    public PurchaseYearResponseDTO(String month, Long purchase) {
        this.month = month;
        this.purchase = purchase;
    }
}
