package com.billing_system.orders.DTO.Response;

import lombok.Data;

@Data
public class SalesOverviewYearResponseDTO {
    String month;
    Long sales;

    public SalesOverviewYearResponseDTO(String month, Long sales) {
        this.month = month;
        this.sales = sales;
    }
}
