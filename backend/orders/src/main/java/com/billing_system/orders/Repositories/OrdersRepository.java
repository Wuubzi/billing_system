package com.billing_system.orders.Repositories;

import com.billing_system.orders.DTO.Response.OrdersResponseDTO;
import com.billing_system.orders.DTO.Response.OrdersSummaryResponseDTO;
import com.billing_system.orders.DTO.Response.SalesOverviewYearResponseDTO;
import com.billing_system.orders.Entities.Orders;
import com.billing_system.orders.Interfaces.OrderProductInterface;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @Query(value = """
    SELECT COALESCE(SUM(p.sale_price * po.quantity), 0) AS revenue
    FROM orders o
    JOIN product_orders po ON o.id_order = po.id_order
    JOIN products p ON p.id_product = po.id_product
    WHERE o.start_date >= :startDate
      AND o.start_date < :endDate
      AND o.status = 'Completed'
    """, nativeQuery = true)
    BigDecimal getRenueve(@Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate);

    @Query(value = """
    SELECT COALESCE(SUM(p.price * po.quantity), 0) AS cost
    FROM orders o
    JOIN product_orders po ON o.id_order = po.id_order
    JOIN products p ON p.id_product = po.id_product
    WHERE o.start_date >= :startDate
      AND o.start_date < :endDate
      AND o.status = 'Completed'
    """, nativeQuery = true)
    BigDecimal getCost(@Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate);

        @Query(value = """ 
            WITH months AS (
    SELECT generate_series(1, 12) AS month_number
    ),
    orders_per_month AS (
    SELECT
        EXTRACT(MONTH FROM start_date::date) AS month_number,
        COUNT(*) AS sales
    FROM orders
    WHERE EXTRACT(YEAR FROM start_date::date) = :year
    GROUP BY month_number
    )
    SELECT\s
        TO_CHAR(TO_DATE(m.month_number::text, 'MM'), 'Month') AS month,
        COALESCE(o.sales, 0) AS sales
    FROM months m
    LEFT JOIN orders_per_month o ON m.month_number = o.month_number
    ORDER BY m.month_number;
    """, nativeQuery = true)
    List<SalesOverviewYearResponseDTO> countSalesOfYear(@Param("year") Integer year);
    @Query(value =
            """
                    WITH months AS (
                SELECT generate_series(1, 12) AS month_number
            ),
                 orders_per_month_status AS (
                     SELECT
                         EXTRACT(MONTH FROM start_date::date) AS month_number,
                         status,
                         COUNT(*) AS sales
                     FROM orders
                     WHERE EXTRACT(YEAR FROM start_date::date) = :year
                     GROUP BY month_number, status
                 ),
                 pivot AS (
                     SELECT
                         m.month_number,
                         TO_CHAR(TO_DATE(m.month_number::text, 'MM'), 'TMMonth') AS month,
                         COALESCE(SUM(CASE WHEN o.status = 'Completed' THEN o.sales END), 0) AS completed,
                         COALESCE(SUM(CASE WHEN o.status = 'Canceled' THEN o.sales END), 0) AS canceled
                     FROM months m
                              LEFT JOIN orders_per_month_status o ON m.month_number = o.month_number
                         GROUP BY m.month_number
                 )
            SELECT month, completed, canceled
            FROM pivot
            ORDER BY month_number;
            """,
            nativeQuery = true)
    List<OrdersSummaryResponseDTO> OrdersSummary(@Param("year") Integer year);

    Integer countOrdersByStatus(String status);

    @Query(value = """
SELECT
    o.id_order AS id_order,
    o.subtotal AS subtotal,
    o.status as status,
    po.quantity as quantity,
    p.name as product_name,
    p.sale_price as product_price
FROM orders o
JOIN product_orders po ON o.id_order = po.id_order
JOIN products p ON po.id_product = p.id_product
ORDER BY o.id_order;
""", nativeQuery = true)
    Page<OrderProductInterface> getOrders(Pageable pageable);

    @Query(value = """
SELECT
    o.id_order AS id_order,
    o.subtotal AS subtotal,
    o.status as status,
    po.quantity as quantity,
    p.name as product_name,
    p.sale_price as product_price
FROM orders o
JOIN product_orders po ON o.id_order = po.id_order
JOIN products p ON po.id_product = p.id_product
WHERE o.id_order = :id_order
ORDER BY o.id_order;
""", nativeQuery = true)
    List<OrderProductInterface> getOrder(@Param("id_order") Long id_order);

}

