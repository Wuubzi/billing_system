package com.billing_system.products.DTO.Request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductsRequestDTO {

    @NotBlank(message = "The name field cannot be blank")
    private String name;
    private String description;
    @NotNull
    @Positive
    private BigDecimal price;
    @NotNull
    @Positive
    private BigDecimal sale_price;
    @PositiveOrZero
    @NotNull
    private Long id_category;
    private String image;
    @NotNull
    @PositiveOrZero
    private Long id_supplier;
}
