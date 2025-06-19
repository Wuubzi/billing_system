package com.billing_system.orders.Repositories;

import com.billing_system.orders.Entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Products, Long> {
}
