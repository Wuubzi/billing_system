package com.billing_system.products.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class DetailsProductResponseDTO {
    private Long id_product;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal sale_price;
    private String image;
    private Integer stock;
    private String category;
    private String supplier_name;
    private String supplier_number;
    private Date expiration_date;


    public DetailsProductResponseDTO(Long id_product, String name, String description, BigDecimal price, BigDecimal sale_price, String image, Integer stock,String category, String supplier_name, String supplier_number, Date expiration_date) {
        this.id_product = id_product;
        this.name = name;
        this.description = description;
        this.price = price;
        this.sale_price = sale_price;
        this.image = image;
        this.stock = stock;
        this.category = category;
        this.supplier_name = supplier_name;
        this.supplier_number = supplier_number;
        this.expiration_date = expiration_date;

    }
}
