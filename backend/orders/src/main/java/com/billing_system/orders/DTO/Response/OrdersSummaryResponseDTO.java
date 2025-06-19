package com.billing_system.orders.DTO.Response;

import com.rabbitmq.tools.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class OrdersSummaryResponseDTO {
    private String month;
    private BigDecimal completed;
    private BigDecimal canceled;



    public OrdersSummaryResponseDTO(String month, BigDecimal completed, BigDecimal canceled) {
        this.month = month;
        this.completed = completed;
        this.canceled = canceled;
    }


  }
