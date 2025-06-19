package com.billing_system.purchases.DTO.Response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseReportsResponseDTO {
 private Integer purchase;
 private BigDecimal cost;
 private Integer cancel;
 private BigDecimal Return;
}

