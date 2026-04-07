package com.mrs.enpoint.feature.refund.service;

import com.mrs.enpoint.feature.refund.dto.RefundResponseDTO;

import java.util.List;

public interface RefundService {

    void processAutoRefund(int paymentId);

    List<RefundResponseDTO> getMyRefunds();

    List<RefundResponseDTO> getAllRefunds();

    RefundResponseDTO getRefundById(int refundId);
}