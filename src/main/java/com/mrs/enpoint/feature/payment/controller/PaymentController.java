package com.mrs.enpoint.feature.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrs.enpoint.feature.payment.dto.PaymentResponseDTO;
import com.mrs.enpoint.feature.payment.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	private final PaymentService paymentService;
	 
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable int id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }
    
    @PostMapping("/{id}/retry-attempts")
    public ResponseEntity<PaymentResponseDTO> retryPayment(@PathVariable int id) {
        return ResponseEntity.ok(paymentService.retryPayment(id));
    }
}
