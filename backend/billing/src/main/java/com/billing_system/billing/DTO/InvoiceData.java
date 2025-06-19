package com.billing_system.billing.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceData {
    private String number;
    private String status;
    private String createdAt;
    private CompanyData company;
    private ClientData client;
    private List<InvoiceItem> items;
    private Double subtotal;
    private Double taxRate;
    private Double taxAmount;
    private Double total;

    public InvoiceData() {
        this.company = createDefaultCompany();
        this.taxRate = 19.0;
    }

    private CompanyData createDefaultCompany() {
        CompanyData company = new CompanyData();
        company.setName("Tu Empresa S.A.S.");
        company.setAddress("Calle Principal 123");
        company.setPhone("(+57) 300-123-4567");
        company.setLocation("Soledad, Atlántico, Colombia");
        company.setNit("900.123.456-7");
        company.setEmail("contacto@tuempresa.com");
        return company;
    }

    public void calculateTotals() {
        this.subtotal = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        this.taxAmount = subtotal * (taxRate / 100);
        this.total = subtotal + taxAmount;
    }
}
