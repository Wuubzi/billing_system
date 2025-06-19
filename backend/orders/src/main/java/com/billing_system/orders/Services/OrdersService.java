package com.billing_system.orders.Services;

import com.billing_system.orders.DTO.Request.CreateOrderRequestDTO;
import com.billing_system.orders.DTO.Request.EditOrderRequestDTO;
import com.billing_system.orders.DTO.Request.OrderRequestDTO;
import com.billing_system.orders.DTO.Response.*;
import com.billing_system.orders.Entities.Orders;
import com.billing_system.orders.Entities.OrdersProducts;
import com.billing_system.orders.Entities.Products;
import com.billing_system.orders.Interfaces.OrderProductInterface;
import com.billing_system.orders.Repositories.OrdersProductsRepository;
import com.billing_system.orders.Repositories.OrdersRepository;
import com.billing_system.orders.Repositories.ProductRepository;
import com.billing_system.orders.Utils.DateFormat;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;
    private final OrdersProductsRepository ordersProductsRepository;
    private final DateFormat dateFormat;

    @Autowired
    public OrdersService(OrdersRepository ordersRepository,
                         ProductRepository productRepository,
                         OrdersProductsRepository ordersProductsRepository,
                         DateFormat dateFormat) {
        this.ordersRepository = ordersRepository;
        this.productRepository = productRepository;
        this.ordersProductsRepository = ordersProductsRepository;
        this.dateFormat = dateFormat;
    }

    public SalesOverviewResponseDTO SalesOverview(String date) {

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
        // Calculate the number of orders
        Integer sales = ordersRepository.countOrdersByStatus("Completed");
        // Calculate the revenue and cost
        BigDecimal revenue = ordersRepository.getRenueve(firstDayOfMonth, lastDayOfMonth);
        BigDecimal cost = ordersRepository.getCost(firstDayOfMonth, lastDayOfMonth);
        // Create the response DTO
        SalesOverviewResponseDTO salesOverviewResponseDTO = new SalesOverviewResponseDTO();
        salesOverviewResponseDTO.setRevenue(revenue);
        salesOverviewResponseDTO.setSales(sales);
        salesOverviewResponseDTO.setCost(cost);
        salesOverviewResponseDTO.setProfit(revenue.subtract(cost));
        return salesOverviewResponseDTO;
    }

    public List<SalesOverviewYearResponseDTO> SalesOverviewYear(String year) {

        if (!year.matches("\\d{4}")) {
            throw new IllegalArgumentException("Invalid year format. Please use 'yyyy'.");
        }

        Integer yearInt = Integer.parseInt(year);
        return ordersRepository.countSalesOfYear(yearInt);
    }

    public List<OrdersSummaryResponseDTO> OrdersSummary(String year) {
        if (!year.matches("\\d{4}")) {
            throw new IllegalArgumentException("Invalid year format. Please use 'yyyy'.");
        }
        Integer yearInt = Integer.parseInt(year);
        return ordersRepository.OrdersSummary(yearInt);
    }

    public Page<OrdersResponseDTO> getOrders(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderProductInterface> flatPage = ordersRepository.getOrders(pageable);


        Map<Long, OrdersResponseDTO> dtoMap = new LinkedHashMap<>();

        for (OrderProductInterface row : flatPage.getContent()) {
            Long orderId = row.getId_order();

            OrdersProductsDTO ordersProductsDTO = new OrdersProductsDTO(
                    row.getQuantity(),
                    row.getProduct_name(),
                    row.getProduct_price()
            );

            OrdersResponseDTO dto = dtoMap.get(orderId);
            if (dto == null) {
                dto = new OrdersResponseDTO();
                dto.setId_order(orderId);
                dto.setSubtotal(row.getSubtotal());
                dto.setStatus(row.getStatus());
                dto.setOrdersProducts(new ArrayList<>());
                dtoMap.put(orderId, dto);
            }

            dto.getOrdersProducts().add(ordersProductsDTO);
        }

        List<OrdersResponseDTO> dtoList = new ArrayList<>(dtoMap.values());

        return new PageImpl<>(dtoList, pageable, flatPage.getTotalElements());
    }

    public OrdersResponseDTO getOrder(Long orderId) {
        List<OrderProductInterface> rows = ordersRepository.getOrder(orderId);

        if (rows.isEmpty()) {
            throw new IllegalArgumentException("Order Not Found");
        }

        OrdersResponseDTO dto = new OrdersResponseDTO();
        List<OrdersProductsDTO> productList = new ArrayList<>();

        for (OrderProductInterface row : rows) {
            if (dto.getId_order() == null) {
                dto.setId_order(row.getId_order());
                dto.setSubtotal(row.getSubtotal());
                dto.setStatus(row.getStatus());
            }

            OrdersProductsDTO product = new OrdersProductsDTO(
                    row.getQuantity(),
                    row.getProduct_name(),
                    row.getProduct_price()
            );

            productList.add(product);
        }

        dto.setOrdersProducts(productList);
        return dto;
    }

    @Transactional
    public ResponseDTO addOrder(CreateOrderRequestDTO orderRequestDTO, HttpServletRequest request) {
        Orders order = new Orders();
        Orders orderSaved = ordersRepository.save(order);


        double subtotal = 0.0;

        for (OrderRequestDTO dto : orderRequestDTO.getProducts()  ) {
            Products product = productRepository.findById(dto.getId_product())
                    .orElseThrow(() -> new IllegalArgumentException("Product Not Found"));

            if (dto.getQuantity() > product.getStock()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }

            OrdersProducts po = new OrdersProducts();
            po.setOrders(orderSaved);
            po.setProducts(product);
            po.setQuantity(dto.getQuantity());
            System.out.println(po);
            ordersProductsRepository.save(po);
            subtotal += product.getSale_price() * dto.getQuantity();
        }


        order.setSubtotal(subtotal);
        ordersRepository.save(order);
        return getResponseDTO("P-201", "Order Added Successfully", request);
    }


    @Transactional
    public ResponseDTO editOrder(Long id_order, EditOrderRequestDTO orderRequestDTO, HttpServletRequest request) {
        Orders order = ordersRepository.findById(id_order)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        List<OrdersProducts> existingProducts = ordersProductsRepository.findByOrders(order);

        Map<Long, Integer> existingMap = existingProducts.stream()
                .collect(Collectors.toMap(
                        op -> op.getProducts().getId_product(),
                        OrdersProducts::getQuantity
                ));

        Map<Long, Integer> incomingMap = orderRequestDTO.getProducts().stream()
                .collect(Collectors.toMap(
                        OrderRequestDTO::getId_product,
                        OrderRequestDTO::getQuantity,
                        (q1, q2) -> q2
                ));

        if (existingMap.equals(incomingMap) && order.getStatus().equals(orderRequestDTO.getStatus())) {
            return getResponseDTO("P-304", "No changes detected. Order not updated.", request);
        }

        ordersProductsRepository.deleteByOrders(order);

        double subtotal = 0.0;
        for (OrderRequestDTO dto : orderRequestDTO.getProducts()) {
            Products product = productRepository.findById(dto.getId_product())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            if (dto.getQuantity() > product.getStock()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }
            OrdersProducts po = new OrdersProducts();
            po.setOrders(order);
            po.setProducts(product);
            po.setQuantity(dto.getQuantity());

            subtotal += product.getSale_price() * dto.getQuantity();

            ordersProductsRepository.save(po);
        }

        order.setSubtotal(subtotal);
        order.setStatus(orderRequestDTO.getStatus());
        ordersRepository.save(order);

        return getResponseDTO("P-200", "Order Updated Successfully", request);
    }

    public  ResponseDTO cancelOrder(Long id_order, HttpServletRequest request) {
        Orders order = ordersRepository.findById(id_order)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getStatus().equals("Cancelled")) {
            throw new IllegalArgumentException("Order already cancelled");
        }

        order.setStatus("Cancelled");
        ordersRepository.save(order);

        return getResponseDTO("P-200", "Order Cancelled Successfully", request);
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
