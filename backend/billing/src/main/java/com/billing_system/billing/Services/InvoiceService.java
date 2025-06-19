package com.billing_system.billing.Services;

import com.billing_system.billing.DTO.Request.InvoiceRequestDTO;
import com.billing_system.billing.DTO.Response.BillingResponse;
import com.billing_system.billing.DTO.Response.ResponseDTO;
import com.billing_system.billing.Entities.Billing;
import com.billing_system.billing.Entities.Orders;
import com.billing_system.billing.Repositories.BillingRepository;
import com.billing_system.billing.Repositories.OrdersRepository;
import com.billing_system.billing.Utils.DateFormat;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    private final BillingRepository billingRepository;
    private final OrdersRepository ordersRepository;
    private final DateFormat dateFormat;

    @Autowired
    public InvoiceService(BillingRepository billingRepository,
                          OrdersRepository ordersRepository,
                          DateFormat dateFormat) {
        this.billingRepository = billingRepository;
        this.ordersRepository = ordersRepository;
        this.dateFormat = dateFormat;
    }

    public Page<BillingResponse> getInvoices(int page, int size) {

        Pageable pageable = PageRequest.of(page,size);

        return billingRepository.getBillings(pageable);

    }

    @Transactional
    public ResponseDTO createInvoice(InvoiceRequestDTO invoiceRequestDTO, HttpServletRequest request) {
        List<Orders> ordersList = new ArrayList<>(
                invoiceRequestDTO.getOrders().stream()
                        .map(orderDto -> {
                            Orders order = ordersRepository.findById(orderDto.getId_order())
                                    .orElseThrow(() -> new IllegalArgumentException("Order Not Found"));
                            if (!"Completed".equals(order.getStatus())) {
                                throw new IllegalStateException("Order is not completed");
                            }
                            return order;
                        })
                        .toList()
        );

        Billing billing = new Billing();
        billing = billingRepository.save(billing);
        billingRepository.flush();

        billing.setOrders(ordersList);
        billingRepository.save(billing);


        return getResponseDTO("P-200", "Invoice created successfully", request);
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
