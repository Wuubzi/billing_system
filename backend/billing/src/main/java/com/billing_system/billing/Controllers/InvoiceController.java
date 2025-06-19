package com.billing_system.billing.Controllers;

import com.billing_system.billing.DTO.Request.InvoiceRequestDTO;
import com.billing_system.billing.DTO.Response.BillingResponse;
import com.billing_system.billing.DTO.Response.ResponseDTO;
import com.billing_system.billing.Services.InvoicePdfService;
import com.billing_system.billing.Services.InvoiceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/billing")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoicePdfService invoicePdfService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService,
                             InvoicePdfService invoicePdfService) {
        this.invoiceService = invoiceService;
        this.invoicePdfService = invoicePdfService;
    }

    @GetMapping("/get-invoices")
    public ResponseEntity<Page<BillingResponse>> getInvoices(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "9") int size) {
        return new ResponseEntity<>(invoiceService.getInvoices(page,size), HttpStatus.OK);
    }

    @PostMapping("/create-invoice")
    public ResponseEntity<ResponseDTO>  createInvoice(@Valid @RequestBody InvoiceRequestDTO invoiceRequestDTO, HttpServletRequest request) {
        return new ResponseEntity<>(invoiceService.createInvoice(invoiceRequestDTO,request), HttpStatus.CREATED);
    }

    @GetMapping("/get-invoice-pdf")
    public ResponseEntity<byte[]> getInvoicePdf(@RequestParam Long id_invoice) throws IOException {
        try {
            byte[] pdfBytes = invoicePdfService.generateInvoicePdf(id_invoice);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "factura-" + id_invoice + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generando PDF: " + e.getMessage()).getBytes());
        }
    }
}
