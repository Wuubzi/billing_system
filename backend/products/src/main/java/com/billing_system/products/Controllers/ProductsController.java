package com.billing_system.products.Controllers;

import com.billing_system.products.DTO.Request.ProductsRequestDTO;
import com.billing_system.products.DTO.Response.*;
import com.billing_system.products.Entities.Products;
import com.billing_system.products.Services.ProductsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductsController {

    private final ProductsService productsService;

    @Autowired
    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping("/inventory-summary")
    public ResponseEntity<InventorySummaryResponseDTO> InventorySummary() {
        return new ResponseEntity<>(productsService.getInventorySummary(), HttpStatus.OK);
    }

    @GetMapping("/products-summary")
    public ResponseEntity<ProductsSummaryResponseDTO> ProductsSummary() {
        return new ResponseEntity<>(productsService.getProductsSummary(), HttpStatus.OK);
    }

    @GetMapping("/top-selling-products")
    public ResponseEntity<Page<BestSellingProducts>> TopSellingProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        return new ResponseEntity<>(productsService.TopSellingProducts(page,size), HttpStatus.OK);
    }

    @GetMapping("/low-stock-products")
    public ResponseEntity<Page <LowStockProducts> > LowStockProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        return new ResponseEntity<>(productsService.LowStockProducts(page,size), HttpStatus.OK);
    }

    @GetMapping("/get-products")
    public ResponseEntity<Page<Products>> getProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size) {
        Page<Products> products = productsService.getAllProducts(page,size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/get-product")
    public ResponseEntity<DetailsProductResponseDTO> getProduct(@RequestParam Long id_product) {
        return new ResponseEntity<>(productsService.getProductById(id_product), HttpStatus.OK);
    }

    @PostMapping("/add-product")
    public ResponseEntity<ResponseDTO> addProduct(@Valid @RequestBody ProductsRequestDTO productDTO, HttpServletRequest request) {
        return new ResponseEntity<>(productsService.addProduct(productDTO, request), HttpStatus.CREATED);
    }

    @PutMapping("/edit-product")
    public ResponseEntity<ResponseDTO> editProduct(@RequestParam Long id_product, @Valid @RequestBody ProductsRequestDTO productDTO, HttpServletRequest request) {
        return new ResponseEntity<>(productsService.editProduct(id_product, productDTO, request), HttpStatus.OK);
    }

    @PutMapping("/inactive-product")
    public ResponseEntity<ResponseDTO> inactiveProduct(@RequestParam Long id_product, HttpServletRequest request) {
        return new ResponseEntity<>(productsService.inactiveProduct(id_product, request), HttpStatus.OK);
    }




}