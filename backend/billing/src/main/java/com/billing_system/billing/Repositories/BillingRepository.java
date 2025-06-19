package com.billing_system.billing.Repositories;

import com.billing_system.billing.DTO.Response.BillingResponse;
import com.billing_system.billing.Entities.Billing;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {


    @Query(value = """
SELECT b.id_billing, b.status, b.created_at,
       o.subtotal, op.quantity, o.id_order,
       p.name, p.sale_price
FROM billings b
JOIN public.orders_billings ob on b.id_billing = ob.id_billing
JOIN public.orders o on ob.id_order = o.id_order
JOIN orders_products op on o.id_order = op.id_order
JOIN products p on op.id_product = p.id_product
""", nativeQuery = true)
    Page<BillingResponse> getBillings(Pageable pageable);

    @Query(value = """
SELECT b.id_billing, b.status, b.created_at,
       o.subtotal, op.quantity, o.id_order,
       p.name, p.sale_price
FROM billings b
JOIN public.orders_billings ob on b.id_billing = ob.id_billing
JOIN public.orders o on ob.id_order = o.id_order
JOIN orders_products op on o.id_order = op.id_order
JOIN products p on op.id_product = p.id_product
WHERE b.id_billing = :id_billing
""", nativeQuery = true)
    List<BillingResponse> getBilling(@Param("id_billing") Long id_billing);
}