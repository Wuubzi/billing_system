package com.billing_system.suppliers.Repositories;

import com.billing_system.suppliers.Entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Products, Long> {

}
