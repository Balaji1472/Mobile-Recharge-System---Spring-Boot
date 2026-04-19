package com.mrs.enpoint.feature.refund.mapper;

import com.mrs.enpoint.entity.Refund;
import com.mrs.enpoint.feature.refund.dto.RefundResponseDTO;

public class RefundMapper {

	private RefundMapper() {
	}

	public static RefundResponseDTO toResponseDTO(Refund refund) {
		RefundResponseDTO dto = new RefundResponseDTO();
		dto.setRefundId(refund.getRefundId());
		dto.setPaymentId(refund.getPayment().getPaymentId());
		dto.setRechargeId(refund.getPayment().getRechargeTransaction().getRechargeId());
		dto.setUserId(refund.getPayment().getRechargeTransaction().getUser().getUserId());
		dto.setFullName(refund.getPayment().getRechargeTransaction().getUser().getFullName());
		dto.setMobileNumber(refund.getPayment().getRechargeTransaction().getConnection().getMobileNumber());
		dto.setPlanName(refund.getPayment().getRechargeTransaction().getPlan().getPlanName());
		dto.setPaymentMethod(refund.getPayment().getPaymentMethod());
		dto.setAmount(refund.getAmount());
		dto.setStatus(refund.getStatus());
		dto.setProcessedAt(refund.getProcessedAt());
		return dto;
	}
}