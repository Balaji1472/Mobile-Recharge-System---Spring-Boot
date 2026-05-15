
package com.mrs.enpoint.feature.payment.controller;

import com.mrs.enpoint.feature.payment.dto.PaymentResponseDTO;
import com.mrs.enpoint.feature.payment.dto.PaymentVerifyRequestDTO;
import com.mrs.enpoint.feature.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/verify")
    public ResponseEntity<PaymentResponseDTO> verifyPayment(
            @Valid @RequestBody PaymentVerifyRequestDTO request) {
        return ResponseEntity.ok(paymentService.verifyAndUpdatePayment(request));
    }
    
    @PostMapping("/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> cancelPayment(@RequestParam String razorpayOrderId) {
        paymentService.cancelPayment(razorpayOrderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/retry-attempts")
    public ResponseEntity<PaymentResponseDTO> retryPayment(@PathVariable int id) {
        return ResponseEntity.ok(paymentService.retryPayment(id));
    }
}
