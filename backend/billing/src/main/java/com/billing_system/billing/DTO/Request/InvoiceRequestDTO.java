package com.billing_system.billing.DTO.Request;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceRequestDTO {
    List<OrdersRequestDTO> orders;
}
