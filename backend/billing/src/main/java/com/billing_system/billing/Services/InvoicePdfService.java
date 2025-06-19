package com.billing_system.billing.Services;

import com.billing_system.billing.DTO.ClientData;
import com.billing_system.billing.DTO.InvoiceData;
import com.billing_system.billing.DTO.InvoiceItem;
import com.billing_system.billing.DTO.Response.BillingResponse;
import com.billing_system.billing.Repositories.BillingRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoicePdfService {
    private final TemplateEngine templateEngine;
    private final BillingRepository billingRepository;

    @Autowired
    public InvoicePdfService(TemplateEngine templateEngine,
                             BillingRepository billingRepository) {
        this.templateEngine = templateEngine;
        this.billingRepository = billingRepository;
    }

    public byte[] generateInvoicePdf(Long id_invoice) throws IOException {
        // 1. Obtener datos de la base de datos
        List<BillingResponse> billingData = billingRepository.getBilling(id_invoice);

        if (billingData.isEmpty()) {
            throw new IOException("No se encontraron datos para la factura: " + id_invoice);
        }

        // 2. Convertir datos a modelo de factura
        InvoiceData invoice = convertToInvoiceData(billingData);

        // 3. Procesar template con Thymeleaf
        Context context = new Context();
        context.setVariable("invoice", invoice);

        String html = templateEngine.process("invoice", context);

        // 4. Convertir HTML a PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
        } catch (Exception e) {
            throw new IOException("Error generando PDF: " + e.getMessage(), e);
        }

        return outputStream.toByteArray();
    }

    private InvoiceData convertToInvoiceData(List<BillingResponse> billingData) {
        // Tomar el primer elemento para datos generales
        BillingResponse firstBilling = billingData.get(0);

        InvoiceData invoice = new InvoiceData();

        // Datos generales de la factura
        invoice.setNumber(String.valueOf(firstBilling.getId_billing()));
        invoice.setStatus(firstBilling.getBilling_status());
        invoice.setCreatedAt(formatDate(String.valueOf(firstBilling.getBilling_created_at())));

        // Cliente (por ahora datos por defecto, puedes obtenerlos de otra tabla)
        ClientData client = createDefaultClient();
        invoice.setClient(client);

        // Convertir productos a items
        List<InvoiceItem> items = billingData.stream()
                .map(this::convertToInvoiceItem)
                .collect(Collectors.toList());

        invoice.setItems(items);

        // Calcular totales
        invoice.calculateTotals();

        return invoice;
    }

    private InvoiceItem convertToInvoiceItem(BillingResponse billing) {
        return new InvoiceItem(
                billing.getProduct_name(),
                billing.getQuantity(),
                billing.getProduct_sale_price()
        );
    }

    private ClientData createDefaultClient() {
        ClientData client = new ClientData();
        client.setName("Cliente General");
        client.setAddress("Dirección del Cliente");
        client.setPhone("(+57) 300-000-0000");
        client.setLocation("Soledad, Atlántico");
        client.setDocument("12345678");
        client.setEmail("cliente@email.com");
        return client;
    }

    private String formatDate(String dateString) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateString.replace("Z", ""));
            return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        } catch (Exception e) {
            return dateString;
        }
    }

}
