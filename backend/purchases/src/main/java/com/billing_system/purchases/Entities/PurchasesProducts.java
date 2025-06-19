package com.billing_system.purchases.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "purchases_products")
public class PurchasesProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_purchase_product;

    @ManyToOne
    @JoinColumn(name = "id_purchase")
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name = "id_product")
    private Products products;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "start_date")
    private Date startDate = new Date();
    @Column(name = "expiration_date")
    private Date expirationDate;



}
