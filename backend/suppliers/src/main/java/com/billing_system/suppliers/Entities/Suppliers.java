package com.billing_system.suppliers.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
public class Suppliers {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id_supplier;
    @Column(name = "name")
    private String name;
    @Column(name = "number")
    private String number;
    @Column(name = "email")
    private String email;
    @Column(name = "status")
    private String status;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Products> products;

}
