package com.billing_system.suppliers.DTO.Response;

import com.billing_system.suppliers.Entities.Products;
import lombok.Data;

@Data
public class SupplierResponseDTO {
    private Long id_supplier;
    private String name;
    private String email;
    private String number;
    private String status;
    private Long id_product;
    private String product_name;

    public SupplierResponseDTO(Long id_supplier, String name, String email, String number, String status, Long id_product, String product_name) {
        this.id_supplier = id_supplier;
        this.name = name;
        this.email = email;
        this.number = number;
        this.status = status;
        this.id_product = id_product;
        this.product_name = product_name;
    }
}
