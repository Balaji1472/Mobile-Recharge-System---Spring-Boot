package com.mrs.enpoint.feature.invoice.service;

import com.mrs.enpoint.feature.invoice.dto.InvoiceResponseDTO;

import java.util.List;

public interface InvoiceService {

    void generateInvoice(int rechargeId);

    InvoiceResponseDTO getInvoiceById(int invoiceId);

    List<InvoiceResponseDTO> getMyInvoices();

    InvoiceResponseDTO getInvoiceByRechargeId(int rechargeId);
}