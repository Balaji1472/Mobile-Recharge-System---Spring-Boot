package com.mrs.enpoint.feature.payment.mapper;

import com.mrs.enpoint.entity.Payment;
import com.mrs.enpoint.feature.payment.dto.PaymentResponseDTO;

public class PaymentMapper {

    private PaymentMapper() {}

    public static PaymentResponseDTO toResponseDTO(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setRechargeId(payment.getRechargeTransaction().getRechargeId());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getStatus());
        dto.setTransactionReference(payment.getTransactionReference());
        dto.setAttemptNumber(payment.getAttemptNumber());
        dto.setFailureReason(payment.getFailureReason());
        dto.setPaymentTime(payment.getPaymentTime());
        return dto;
    }
}