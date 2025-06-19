package com.billing_system.orders.DTO.Response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesOverviewResponseDTO {
    private Integer sales;
    private BigDecimal revenue;
    private BigDecimal profit;
    private BigDecimal Cost;
}
