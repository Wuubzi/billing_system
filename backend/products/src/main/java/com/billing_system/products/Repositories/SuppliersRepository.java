package com.billing_system.products.Repositories;

import com.billing_system.products.Entities.Suppliers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuppliersRepository extends JpaRepository<Suppliers, Long> {
}
