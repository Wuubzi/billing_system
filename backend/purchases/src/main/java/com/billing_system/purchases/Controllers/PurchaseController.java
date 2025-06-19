package com.billing_system.purchases.Controllers;

import com.billing_system.purchases.DTO.Request.PurchaseEditRequestDTO;
import com.billing_system.purchases.DTO.Request.PurchaseRequestDTO;
import com.billing_system.purchases.DTO.Response.PurchaseReportsResponseDTO;
import com.billing_system.purchases.DTO.Response.PurchaseResponseDTO;
import com.billing_system.purchases.DTO.Response.PurchaseYearResponseDTO;
import com.billing_system.purchases.DTO.Response.ResponseDTO;
import com.billing_system.purchases.Services.PurchaseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/purchase-overview")
    public ResponseEntity<PurchaseReportsResponseDTO> getPurchaseOverview(@RequestParam String date) {
        return new ResponseEntity<>(purchaseService.getPurchaseOverview(date), HttpStatus.OK);
    }

    @GetMapping("/purchases-year")
    public ResponseEntity<List<PurchaseYearResponseDTO>> getPurchaseYear(@RequestParam String year ){
        return new ResponseEntity<>(purchaseService.PurchaseYear(year), HttpStatus.OK);
    }

    @GetMapping("/get-purchases")
    public ResponseEntity<Page<PurchaseResponseDTO>> getPurchases(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size) {
        return new ResponseEntity<>(purchaseService.getPurchases(page, size), HttpStatus.OK);
    }

    @GetMapping("/get-purchase")
    public ResponseEntity<PurchaseResponseDTO> getPurchase(@RequestParam Long id_purchase){
        return new ResponseEntity<>(purchaseService.getPurchaseById(id_purchase), HttpStatus.OK);
    }

    @PostMapping("/add-purchase")
    public ResponseEntity<ResponseDTO> addPurchase(@Valid @RequestBody PurchaseRequestDTO purchaseRequestDTO, HttpServletRequest request) {
        return new ResponseEntity<>(purchaseService.addPurchase(purchaseRequestDTO, request), HttpStatus.CREATED);
    }
    @PutMapping("/edit-purchase")
    public ResponseEntity<ResponseDTO> editPurchase(@RequestParam Long id_purchase, @Valid @RequestBody PurchaseEditRequestDTO purchaseRequestDTO, HttpServletRequest request) {
        return new ResponseEntity<>(purchaseService.editPurchase(id_purchase, purchaseRequestDTO, request), HttpStatus.OK);
    }
    @PutMapping("/cancel-purchase")
    public ResponseEntity<ResponseDTO> cancelPurchase(@RequestParam Long id_purchase, HttpServletRequest request) {
        return new ResponseEntity<>(purchaseService.cancelPurchase(id_purchase,request), HttpStatus.OK);
    }
}

