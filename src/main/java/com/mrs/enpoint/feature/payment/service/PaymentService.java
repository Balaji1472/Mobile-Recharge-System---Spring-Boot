package com.mrs.enpoint.feature.payment.service;

import com.mrs.enpoint.feature.payment.dto.PaymentResponseDTO;

public interface PaymentService {

    PaymentResponseDTO getPaymentById(int paymentId);
 
    PaymentResponseDTO retryPayment(int rechargeId);

}