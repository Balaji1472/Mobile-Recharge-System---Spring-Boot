package com.mrs.enpoint.feature.invoice.controller;

import com.mrs.enpoint.feature.invoice.dto.InvoiceResponseDTO;
import com.mrs.enpoint.feature.invoice.service.InvoiceService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDTO> getInvoiceById(@PathVariable int id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<InvoiceResponseDTO>> getMyInvoices() {
        return ResponseEntity.ok(invoiceService.getMyInvoices());
    }

    @GetMapping("/recharges/{rechargeId}")
    public ResponseEntity<InvoiceResponseDTO> getInvoiceByRechargeId(
            @PathVariable int rechargeId) {
        return ResponseEntity.ok(invoiceService.getInvoiceByRechargeId(rechargeId));
    }
}