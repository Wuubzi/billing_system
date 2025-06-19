package com.billing_system.products.Services;

import com.billing_system.products.DTO.Request.ProductsRequestDTO;
import com.billing_system.products.DTO.Response.*;
import com.billing_system.products.Entities.Categories;
import com.billing_system.products.Entities.Products;
import com.billing_system.products.Entities.Suppliers;
import com.billing_system.products.Repositories.CategoriesRepository;
import com.billing_system.products.Repositories.ProductsRepository;
import com.billing_system.products.Repositories.SuppliersRepository;
import com.billing_system.products.Utils.DateFormat;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductsService {

    private final ProductsRepository productsRepository;
    private final SuppliersRepository suppliersRepository;
    private final CategoriesRepository categoriesRepository;
    private final DateFormat dateFormat;

    @Autowired
    public ProductsService(ProductsRepository productsRepository,
                           SuppliersRepository suppliersRepository,
                           CategoriesRepository categoriesRepository,
                           DateFormat dateFormat) {
        this.productsRepository = productsRepository;
        this.suppliersRepository = suppliersRepository;
        this.categoriesRepository = categoriesRepository;
        this.dateFormat = dateFormat;
    }

    public InventorySummaryResponseDTO getInventorySummary() {
        InventorySummaryResponseDTO inventorySummaryResponseDTO = new InventorySummaryResponseDTO();
        inventorySummaryResponseDTO.setQuantityInStock(productsRepository.countAllByStatus("In-Stock"));
        inventorySummaryResponseDTO.setQuantityInExpiration(productsRepository.countAllByStatus("In-Expiration"));
        return inventorySummaryResponseDTO;
    }

    public ProductsSummaryResponseDTO getProductsSummary() {
        ProductsSummaryResponseDTO productsSummaryResponseDTO = new ProductsSummaryResponseDTO();
        productsSummaryResponseDTO.setCategories(categoriesRepository.count());
        productsSummaryResponseDTO.setSuppliers(suppliersRepository.count());
        return productsSummaryResponseDTO;
    }

    public Page <BestSellingProducts> TopSellingProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return productsRepository.getbestSellingProducts(pageable);
    }
    public Page<LowStockProducts> LowStockProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return productsRepository.getLowStockProducts(pageable);
    }
    public Page<Products> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productsRepository.findAll(pageable);
    }

    public DetailsProductResponseDTO getProductById(Long id_product) {
        Optional<DetailsProductResponseDTO> productOptional = productsRepository.getProductsDetails(id_product);
        if (productOptional.isPresent()) {
            return productOptional.get();
        } else {
            throw new IllegalArgumentException("Product not found");
        }
    }

    public ResponseDTO addProduct(ProductsRequestDTO productDTO, HttpServletRequest request) {
        Optional<Products> productOptional = productsRepository.findByName(productDTO.getName());
        Optional<Categories> categoryOptional = categoriesRepository.findById(productDTO.getId_category());
        Optional<Suppliers> suppliersOptional = suppliersRepository.findById(productDTO.getId_supplier());
        if (productOptional.isPresent()) {
            throw new IllegalArgumentException("Product already exists");
        }
        if (categoryOptional.isEmpty()) {
            throw new IllegalArgumentException("Category not found");
        }
        if (suppliersOptional.isEmpty()) {
            throw new IllegalArgumentException("Supplier not found");
        }
        Categories category = categoryOptional.get();
        Suppliers suppliers = suppliersOptional.get();
        Products product = new Products();
        getProducts(productDTO, category, suppliers, product);
        return getResponseDTO("201", "Product added successfully", request );
    }

    private void getProducts(ProductsRequestDTO productDTO, Categories category, Suppliers suppliers, Products product) {
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setSale_price(productDTO.getSale_price());
        product.setImage(productDTO.getImage());
        product.setCategories(category);
        product.setSuppliers(suppliers);
        productsRepository.save(product);
    }


    public ResponseDTO editProduct(Long id_product, ProductsRequestDTO productDTO, HttpServletRequest request) {
        Optional<Products> productOptional = productsRepository.findById_product(id_product);
        Optional<Categories> categoryOptional = categoriesRepository.findById(productDTO.getId_category());
        Optional<Suppliers> suppliersOptional = suppliersRepository.findById(productDTO.getId_supplier());
        if (productOptional.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }
        if (categoryOptional.isEmpty()) {
            throw new IllegalArgumentException("Category not found");
        }
        if (suppliersOptional.isEmpty()) {
            throw new IllegalArgumentException("Supplier not found");
        }
        Categories category = categoryOptional.get();
        Products product = productOptional.get();
        Suppliers suppliers = suppliersOptional.get();
        getProducts(productDTO, category, suppliers, product);
        return getResponseDTO("200", "Product updated successfully", request );

    }

    public ResponseDTO inactiveProduct(Long id_product, HttpServletRequest request) {
        Optional<Products> productOptional = productsRepository.findById_product(id_product);
        if (productOptional.isPresent()) {
            Products product = productOptional.get();
            product.setStatus("Inactive");
            productsRepository.save(product);
            return getResponseDTO("200", "Product deleted successfully", request );
        } else {
            throw new IllegalArgumentException("Product not found");
        }

    }


    private ResponseDTO getResponseDTO(String code, String message, HttpServletRequest request) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(code);
        responseDTO.setMessage(message);
        responseDTO.setUri(request.getRequestURI());
        responseDTO.setTimestamp(dateFormat.getDate());
        return responseDTO;
    }

}
