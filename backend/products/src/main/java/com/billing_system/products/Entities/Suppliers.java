package com.billing_system.products.Entities;

import com.billing_system.products.DTO.Response.BestSellingProducts;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Suppliers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_supplier;
    @Column(name = "name")
    private String name;
    @Column(name = "number")
    private String number;
    @Column(name = "email")
    private String email;
    @OneToMany(mappedBy = "suppliers")
    @ToString.Exclude
    @JsonIgnore
    private List<Products> products;


}
