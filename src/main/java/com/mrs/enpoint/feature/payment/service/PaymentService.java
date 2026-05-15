package com.mrs.enpoint.feature.payment.service;

import com.mrs.enpoint.feature.payment.dto.PaymentResponseDTO;
import com.mrs.enpoint.feature.payment.dto.PaymentVerifyRequestDTO;

public interface PaymentService {

    PaymentResponseDTO getPaymentById(int paymentId);
    
    PaymentResponseDTO verifyAndUpdatePayment(PaymentVerifyRequestDTO request);
 
    PaymentResponseDTO retryPayment(int rechargeId);
        
    public void cancelPayment(String razorpayOrderId);

}