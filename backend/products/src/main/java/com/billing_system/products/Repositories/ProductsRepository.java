package com.billing_system.products.Repositories;

import com.billing_system.products.DTO.Response.BestSellingProducts;
import com.billing_system.products.DTO.Response.DetailsProductResponseDTO;
import com.billing_system.products.DTO.Response.LowStockProducts;
import com.billing_system.products.Entities.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    Integer countAllByStatus(String status);
    @Query(value = """
SELECT
    p.name AS product_name,
    SUM(po.quantity) AS total_sold_quantity,
    p.stock AS current_stock,
    p.price AS product_price
FROM products p
         JOIN product_orders po ON p.id_product = po.id_product
         JOIN orders o ON po.id_order = o.id_order
WHERE o.status = 'Completed'
GROUP BY p.id_product, p.name, p.stock, p.price
ORDER BY total_sold_quantity DESC
""", nativeQuery = true)
    Page<BestSellingProducts> getbestSellingProducts(Pageable pageable);


    @Query(value = """
SELECT
    p.name AS product_name,
    p.image AS product_image,
    p.stock AS remaining_stock
FROM products p
WHERE p.status = "low-stock"
ORDER BY p.stock ASC
""", nativeQuery = true)
    Page<LowStockProducts> getLowStockProducts(Pageable pageable);


    @Query(value = """
SELECT
    p.id_product as id_product,
    p.name as name,
    p.description as description,
    p.price as price,
    p.sale_price as sale_price,
    p.image as image,
    p.stock as stock,
    c.name as category,
    s.name as supplier_name,
    s.number as supplier_number,
    pp.expiration_date as expiration_date
FROM
    products p
        JOIN purchases_products pp ON p.id_product = pp.id_product
        LEFT JOIN
    categories c ON p.id_category = c.id_category
        LEFT JOIN
    suppliers s ON p.id_product = s.id_product
WHERE p.id_product = :id
""", nativeQuery = true)
    Optional<DetailsProductResponseDTO> getProductsDetails(@Param("id") Long id);
    @Query(value = "SELECT * FROM products WHERE id_product = :id_product", nativeQuery = true)
    Optional<Products> findById_product(@Param("id_product") Long id_product);
    Optional<Products> findByName(String name);
}
