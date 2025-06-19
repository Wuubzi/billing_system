package com.billing_system.purchases.Repositories;

import com.billing_system.purchases.DTO.Response.PurchaseResponseDTO;
import com.billing_system.purchases.DTO.Response.PurchaseYearResponseDTO;
import com.billing_system.purchases.Entities.Purchase;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query(value = """
    SELECT COALESCE(SUM(p.price * po.quantity), 0) AS cost
    FROM purchases pu
    JOIN purchases_products po ON pu.id_purchase = po.id_purchase
    JOIN products p ON p.id_product = po.id_product
    WHERE po.start_date >= :startDate
      AND po.start_date < :endDate
      AND pu.status = 'Received'
    """, nativeQuery = true)
    BigDecimal getCost(@Param("startDate") LocalDate startDate,
                       @Param("endDate") LocalDate endDate);

    @Query(value = """
    SELECT COALESCE(SUM(p.sale_price * po.quantity), 0) AS cost
    FROM purchases pu
    JOIN purchases_products po ON pu.id_purchase = po.id_purchase
    JOIN products p ON p.id_product = po.id_product
    WHERE po.start_date >= :startDate
      AND po.start_date < :endDate
      AND pu.status = 'Received'
    """, nativeQuery = true)
    BigDecimal getReturn(@Param("startDate") LocalDate startDate,
                       @Param("endDate") LocalDate endDate);
    Integer countPurchaseByStatus(String status);

    @Query(value = """ 
        WITH months AS (
            SELECT generate_series(1, 12) AS month_number
        ),
        orders_per_month AS (
            SELECT
                EXTRACT(MONTH FROM pp.start_date::date) AS month_number,
                COUNT(*) AS purchase
            FROM purchases_products pp
            WHERE EXTRACT(YEAR FROM pp.start_date::date) = ?
            GROUP BY month_number
        )
        SELECT\s
            TO_CHAR(TO_DATE(m.month_number::text, 'MM'), 'Month') AS month,
            COALESCE(o.purchase, 0) AS purchase
        FROM months m
        LEFT JOIN orders_per_month o ON m.month_number = o.month_number
        ORDER BY m.month_number;
""", nativeQuery = true)
    List<PurchaseYearResponseDTO> countSalesOfYear(@Param("year") Integer year);

    @Query(value = """
SELECT
    pu.id_purchase AS id_purchase,
    p.name AS product_name,
    s.name AS supplier_name,
    pp.quantity AS quantity,
    pp.expiration_date AS expiration_date,
    pu.status AS status
FROM purchases pu
JOIN purchases_products pp ON pu.id_purchase = pp.id_purchase
JOIN products p ON pp.id_product = p.id_product
JOIN suppliers s ON p.id_supplier = s.id_supplier
""", nativeQuery = true)
    Page<PurchaseResponseDTO> getPurchases(Pageable pageable);
    @Query(value = """
 SELECT
    pu.id_purchase AS id_purchase,
    p.name AS product_name,
    s.name AS supplier_name,
    pp.quantity AS quantity,
    pp.expiration_date AS expiration_date,
    pu.status AS status
FROM purchases pu
JOIN purchases_products pp ON pu.id_purchase = pp.id_purchase
JOIN products p ON pp.id_product = p.id_product
JOIN suppliers s ON p.id_supplier = s.id_supplier
 WHERE pu.id_purchase = :id_purchase
 """, nativeQuery = true)
    Optional<PurchaseResponseDTO> getPurchaseById(@Param("id_purchase") Long id_purchase);

}
