package com.billing_system.suppliers.Services;

import com.billing_system.suppliers.DTO.Request.SupplierRequestDTO;
import com.billing_system.suppliers.DTO.Response.ResponseDTO;
import com.billing_system.suppliers.DTO.Response.SupplierResponseDTO;
import com.billing_system.suppliers.Entities.Suppliers;
import com.billing_system.suppliers.Repositories.ProductsRepository;
import com.billing_system.suppliers.Repositories.SupplierRepository;
import com.billing_system.suppliers.Utils.DateFormat;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;


@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final DateFormat dateFormat;
    @Autowired
    public SupplierService(SupplierRepository supplierRepository,
                           DateFormat dateFormat) {
        this.supplierRepository = supplierRepository;
        this.dateFormat = dateFormat;
    }

    public Page<SupplierResponseDTO> getAllSuppliers(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return supplierRepository.getAllSuppliers(pageable);
    }

    public SupplierResponseDTO getSupplierById(Long id_supplier) {
        Optional<SupplierResponseDTO> supplierOptional = supplierRepository.GetSupplierById(id_supplier);
        if (supplierOptional.isEmpty()) {
            throw new IllegalArgumentException("Supplier not found");
        }
        return supplierOptional.get();
    }


    public ResponseDTO addSupplier(SupplierRequestDTO supplier, HttpServletRequest request) {
        Optional<Suppliers> supplierOptional = supplierRepository.findByEmail(supplier.getEmail());
        Optional<Suppliers> supplierNumberOptional = supplierRepository.findByNumber(supplier.getNumber());
        if (supplierOptional.isPresent() || supplierNumberOptional.isPresent()) {
            throw  new IllegalArgumentException("Supplier already exists");
        }
        Suppliers newSupplier = new Suppliers();
        newSupplier.setName(supplier.getName());
        newSupplier.setEmail(supplier.getEmail());
        newSupplier.setNumber(supplier.getNumber());
        newSupplier.setStatus("Active");
        supplierRepository.save(newSupplier);
        return getResponseDTO("P-201", "Supplier created successfully", request);
    }

    public ResponseDTO editSupplier(SupplierRequestDTO supplier, HttpServletRequest request) {
        Optional<Suppliers> supplierOptional = supplierRepository.findByEmail(supplier.getEmail());
        if (supplierOptional.isEmpty()) {
            throw new IllegalArgumentException("Supplier not found");
        }
        Suppliers existingSupplier = supplierOptional.get();
        existingSupplier.setName(supplier.getName());
        existingSupplier.setEmail(supplier.getEmail());
        existingSupplier.setNumber(supplier.getNumber());
        if (Objects.equals(existingSupplier.getStatus(), "Inactive")) {
            existingSupplier.setStatus("Inactive");
        } else {
            existingSupplier.setStatus("Active");
        }
        supplierRepository.save(existingSupplier);
        return getResponseDTO("P-200", "Supplier updated successfully", request);
    }

    public ResponseDTO inactiveSupplier(Long id_supplier, HttpServletRequest request) {
        Optional<Suppliers> supplierOptional = supplierRepository.findById(id_supplier);
        if (supplierOptional.isEmpty()) {
            throw new IllegalArgumentException("Supplier not found");
        }
        Suppliers existingSupplier = supplierOptional.get();
        if (existingSupplier.getStatus().equals("Inactive")) {
            throw new IllegalArgumentException("Supplier already inactive");
        }
        existingSupplier.setStatus("Inactive");
        supplierRepository.save(existingSupplier);
        return getResponseDTO("P-200", "Supplier deleted successfully", request);
    }

    private ResponseDTO getResponseDTO(String code, String message, HttpServletRequest request) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(code);
        responseDTO.setMessage(message);
        responseDTO.setUri(request.getRequestURI());
        responseDTO.setTimestamp(String.valueOf(System.currentTimeMillis()));
        responseDTO.setData(dateFormat.getDate());
        return responseDTO;
    }
}
