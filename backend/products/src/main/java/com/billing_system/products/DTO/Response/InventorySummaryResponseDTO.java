package com.billing_system.products.DTO.Response;

import lombok.Data;

@Data
public class InventorySummaryResponseDTO {
    private Integer quantityInStock;
    private Integer quantityInExpiration;
}
