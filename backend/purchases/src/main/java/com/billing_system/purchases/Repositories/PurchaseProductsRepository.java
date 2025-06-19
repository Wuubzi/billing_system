package com.billing_system.purchases.Repositories;

import com.billing_system.purchases.Entities.PurchasesProducts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseProductsRepository extends JpaRepository<PurchasesProducts, Long> {
}
