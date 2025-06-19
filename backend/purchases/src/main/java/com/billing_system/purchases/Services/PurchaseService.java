package com.billing_system.purchases.Services;

import com.billing_system.purchases.DTO.Request.PurchaseEditRequestDTO;
import com.billing_system.purchases.DTO.Request.PurchaseRequestDTO;
import com.billing_system.purchases.DTO.Response.PurchaseReportsResponseDTO;
import com.billing_system.purchases.DTO.Response.PurchaseResponseDTO;
import com.billing_system.purchases.DTO.Response.PurchaseYearResponseDTO;
import com.billing_system.purchases.DTO.Response.ResponseDTO;
import com.billing_system.purchases.Entities.Products;
import com.billing_system.purchases.Entities.Purchase;
import com.billing_system.purchases.Entities.PurchasesProducts;
import com.billing_system.purchases.Purchases;
import com.billing_system.purchases.Repositories.ProductsRepository;
import com.billing_system.purchases.Repositories.PurchaseProductsRepository;
import com.billing_system.purchases.Repositories.PurchaseRepository;
import com.billing_system.purchases.Utils.DateFormat;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ProductsRepository productsRepository;
    private final PurchaseProductsRepository purchaseProductsRepository;
    private final DateFormat dateFormat;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository,
                           DateFormat dateFormat,
                           ProductsRepository productsRepository,
                           PurchaseProductsRepository purchaseProductsRepository) {
        this.purchaseRepository = purchaseRepository;
        this.productsRepository = productsRepository;
        this.purchaseProductsRepository = purchaseProductsRepository;
        this.dateFormat = dateFormat;
    }
    public PurchaseReportsResponseDTO getPurchaseOverview(String date) {
        // Parse the date string to a YearMonth object
        YearMonth yearMonth;
        try {
            yearMonth = YearMonth.parse(date);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM'.");
        }
        // get the first day of the month
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        // get the last day of the month
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth().plusDays(1);

        Integer purchase = purchaseRepository.countPurchaseByStatus("Received");
        Integer cancel = purchaseRepository.countPurchaseByStatus("Cancel");
        BigDecimal cost = purchaseRepository.getCost(firstDayOfMonth, lastDayOfMonth);
        BigDecimal Return = purchaseRepository.getReturn(firstDayOfMonth, lastDayOfMonth);
        PurchaseReportsResponseDTO purchaseReportsResponseDTO = new PurchaseReportsResponseDTO();
        purchaseReportsResponseDTO.setPurchase(purchase);
        purchaseReportsResponseDTO.setCost(cost);
        purchaseReportsResponseDTO.setCancel(cancel);
        purchaseReportsResponseDTO.setReturn(Return);
        return purchaseReportsResponseDTO;
    }

    public List<PurchaseYearResponseDTO> PurchaseYear(String year) {
        if (!year.matches("\\d{4}")) {
            throw new IllegalArgumentException("Invalid year format. Please use 'yyyy'.");
        }
        Integer yearInt = Integer.parseInt(year);

        return purchaseRepository.countSalesOfYear(yearInt);
    }

    public Page<PurchaseResponseDTO> getPurchases(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return purchaseRepository.getPurchases(pageable);
    }

    public PurchaseResponseDTO getPurchaseById(Long id_purchase) {
        Optional<PurchaseResponseDTO> purchaseOptional =  purchaseRepository.getPurchaseById(id_purchase);
        if (purchaseOptional.isEmpty()) {
            throw new IllegalArgumentException("Purchase not found");
        }
        return purchaseOptional.get();
    }

    @Transactional
    public ResponseDTO addPurchase(PurchaseRequestDTO purchaseRequestDTO, HttpServletRequest request) {
        Optional<Products> productsOptional = productsRepository.findById(purchaseRequestDTO.getId_product());
        if (productsOptional.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }
        Products products = productsOptional.get();
        Purchase purchase = new Purchase();
        Purchase purchaseSaved = purchaseRepository.save(purchase);
        PurchasesProducts purchasesProducts = new PurchasesProducts();
        purchasesProducts.setProducts(products);
        purchasesProducts.setPurchase(purchaseSaved);
        purchasesProducts.setQuantity(purchaseRequestDTO.getQuantity());
        purchasesProducts.setExpirationDate(purchaseRequestDTO.getExpiration_date());
        purchaseProductsRepository.save(purchasesProducts);
        return getResponseDTO("P-201", "Purchase Created Successfully", request);
    }

    public ResponseDTO editPurchase(Long id_purchase, PurchaseEditRequestDTO purchaseRequest, HttpServletRequest request) {
        Optional<PurchaseResponseDTO> purchaseOptional = purchaseRepository.getPurchaseById(id_purchase);
        Optional<Products> productsOptional = productsRepository.findById(purchaseRequest.getId_product());
        Purchase purchaseR = null;
        if (purchaseOptional.isEmpty()) {
            throw new IllegalArgumentException("Purchase not found");
        }
        if (productsOptional.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }
        Products product = productsOptional.get();
        PurchaseResponseDTO purchaseResponseDTO = purchaseOptional.get();
        if (!purchaseResponseDTO.getStatus().equals((purchaseRequest.getStatus()))) {
              Optional<Purchase> purchase = purchaseRepository.findById(id_purchase);
              if (purchase.isPresent()) {
                  Purchase purchaseEdit = purchase.get();
                  purchaseEdit.setStatus(purchaseRequest.getStatus());
                  purchaseR = purchaseRepository.save(purchaseEdit);
              }
        }
        PurchasesProducts purchasesProducts = new PurchasesProducts();
        purchasesProducts.setQuantity(purchaseRequest.getQuantity());
        purchasesProducts.setExpirationDate(purchaseRequest.getExpiration_date());
        purchasesProducts.setPurchase(purchaseR);
        purchasesProducts.setProducts(product);
        purchaseProductsRepository.save(purchasesProducts);
        return getResponseDTO("P-200", "Purchase Edited Successfully", request);
    }

    public ResponseDTO cancelPurchase(Long id_purchase,HttpServletRequest request) {
        Optional<Purchase> purchaseOptional = purchaseRepository.findById(id_purchase);
        if (purchaseOptional.isEmpty()) {
            throw new IllegalArgumentException("Purchase not found");
        }
        Purchase purchase = purchaseOptional.get();
        if(purchase.getStatus().equals("Canceled")) {
            throw new IllegalArgumentException("Purchase already canceled");
        }
        purchase.setStatus("Canceled");
        purchaseRepository.save(purchase);
        return getResponseDTO("P-200", "Purchase Canceled Successfully", request);
    }

    private ResponseDTO getResponseDTO(String code, String message, HttpServletRequest request){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(message);
        responseDTO.setCode(code);
        responseDTO.setUri(request.getRequestURI());
        responseDTO.setTimestamp(dateFormat.getDate());
        return responseDTO;
    }

    }
