package com.billing_system.orders.Controllers;

import com.billing_system.orders.DTO.Request.CreateOrderRequestDTO;
import com.billing_system.orders.DTO.Request.EditOrderRequestDTO;
import com.billing_system.orders.DTO.Response.*;
import com.billing_system.orders.Services.OrdersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/orders")
public class OrdersController {

    private final OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping("/sales-overview")
    public ResponseEntity<SalesOverviewResponseDTO> SalesOverview(@RequestParam String date ){
        date = date.trim();
        return new ResponseEntity<>(ordersService.SalesOverview(date), HttpStatus.OK);
    }

    @GetMapping("/sales-year")
    public ResponseEntity<List<SalesOverviewYearResponseDTO>> SalesOverviewYear(@RequestParam String year ) {
        year = year.trim();
        return new ResponseEntity<>(ordersService.SalesOverviewYear(year), HttpStatus.OK);
    }

    @GetMapping("/orders-summary")
    public ResponseEntity<List<OrdersSummaryResponseDTO>> OrdersSummary(@RequestParam  String year) {
        year = year.trim();
        return new ResponseEntity<>(ordersService.OrdersSummary(year), HttpStatus.OK);
    }
    @GetMapping("/get-orders")
    public  ResponseEntity<Page<OrdersResponseDTO>> getOrders(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "9") Integer size) {
        return new ResponseEntity<>(ordersService.getOrders(page,size), HttpStatus.OK) ;
     }

    @GetMapping("/get-order")
    public ResponseEntity<OrdersResponseDTO> getOrder(@RequestParam Long id_order) {
        return new ResponseEntity<>(ordersService.getOrder(id_order), HttpStatus.OK) ;
    }
    @PostMapping("/add-order")
    public ResponseEntity<ResponseDTO> addOrder(@Valid @RequestBody CreateOrderRequestDTO createOrderRequestDTO, HttpServletRequest request) {
        return new ResponseEntity<>(ordersService.addOrder(createOrderRequestDTO, request), HttpStatus.CREATED);
    }

    @PutMapping("/edit-order")
    public ResponseEntity<ResponseDTO> editOrder(@RequestParam Long id_order, @Valid @RequestBody EditOrderRequestDTO editOrderRequestDTO, HttpServletRequest request) {
        return new ResponseEntity<>(ordersService.editOrder(id_order, editOrderRequestDTO, request), HttpStatus.OK);
    }

    @PutMapping("/cancel-order")
    public ResponseEntity<ResponseDTO>  cancelOrder(@RequestParam Long id_order, HttpServletRequest request) {
        return new ResponseEntity<>(ordersService.cancelOrder(id_order, request), HttpStatus.OK) ;
    }
}
