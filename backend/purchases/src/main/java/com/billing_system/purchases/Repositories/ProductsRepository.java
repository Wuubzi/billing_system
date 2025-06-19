package com.billing_system.purchases.Repositories;

import com.billing_system.purchases.Entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products, Long>  {
}
