package com.billing_system.suppliers.Repositories;

import com.billing_system.suppliers.DTO.Response.SupplierResponseDTO;
import com.billing_system.suppliers.Entities.Suppliers;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Suppliers, Long> {
 @Query(value = """
SELECT
    sp.id_supplier AS id_supplier,
    sp.name AS name,
    sp.email AS email,
    sp.number AS number,
    sp.status AS status,
    p.id_product AS id_product,
    p.name AS product_name
    FROM suppliers sp
 JOIN products p ON sp.id_supplier = p.id_supplier;
""", nativeQuery = true)
 Page<SupplierResponseDTO> getAllSuppliers(Pageable pageable);
    @Query(value = """
 SELECT
     sp.id_supplier AS id_supplier,
     sp.name AS name,
     sp.email AS email,
     sp.number AS number,
     sp.status AS status,
     p.id_product AS id_product,
     p.name AS product_name
     FROM suppliers sp
  JOIN products p ON sp.id_supplier = p.id_supplier
 WHERE sp.id_supplier = :id_supplier""", nativeQuery = true)
 Optional<SupplierResponseDTO> GetSupplierById(@Param("id_suplier") Long id_supplier);
 Optional<Suppliers> findByEmail(String email);
 Optional<Suppliers> findByNumber(String number);

}
