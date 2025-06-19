package com.billing_system.suppliers.Controllers;

import com.billing_system.suppliers.DTO.Request.SupplierRequestDTO;
import com.billing_system.suppliers.DTO.Response.ResponseDTO;
import com.billing_system.suppliers.DTO.Response.SupplierResponseDTO;
import com.billing_system.suppliers.Entities.Suppliers;
import com.billing_system.suppliers.Services.SupplierService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierController {
    private final SupplierService supplierService;
    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/get-suppliers")
    public ResponseEntity<Page<SupplierResponseDTO>> getSuppliers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(supplierService.getAllSuppliers(page, size), HttpStatus.OK);
    }
    @GetMapping("/get-supplier")
    public ResponseEntity<SupplierResponseDTO> getSupplier(@RequestParam Long id_supplier) {
        return new ResponseEntity<>(supplierService.getSupplierById(id_supplier), HttpStatus.OK);
    }

    @PostMapping("/add-supplier")
    public ResponseEntity<ResponseDTO> addSupplier(@Valid @RequestBody SupplierRequestDTO supplier, HttpServletRequest request) {
        return new ResponseEntity<>(supplierService.addSupplier(supplier,request), HttpStatus.CREATED);
    }

    @PutMapping("/edit-supplier")
    public ResponseEntity<ResponseDTO>  editSupplier(@Valid @RequestBody SupplierRequestDTO supplier, HttpServletRequest request) {
        return new ResponseEntity<>(supplierService.editSupplier(supplier,request), HttpStatus.OK);
    }
    @PutMapping("/inactive-supplier")
    public ResponseEntity<ResponseDTO>  inactiveSupplier(@RequestParam Long id_supplier, HttpServletRequest request) {
        return new ResponseEntity<>(supplierService.inactiveSupplier(id_supplier,request), HttpStatus.OK);
    }

}
