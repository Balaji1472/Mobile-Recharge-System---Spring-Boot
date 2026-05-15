package com.mrs.enpoint.feature.invoice.mapper;

import com.mrs.enpoint.entity.Payment;
import com.mrs.enpoint.entity.RechargeInvoice;
import com.mrs.enpoint.feature.invoice.dto.InvoiceResponseDTO;

public class InvoiceMapper {

	private InvoiceMapper() {
	}


	public static InvoiceResponseDTO toResponseDTO(RechargeInvoice invoice, Payment payment) {
		InvoiceResponseDTO dto = new InvoiceResponseDTO();

		dto.setInvoiceId(invoice.getInvoiceId());
		dto.setGeneratedAt(invoice.getGeneratedAt());

		dto.setRechargeId(invoice.getRechargeTransaction().getRechargeId());
		dto.setRechargeStatus(invoice.getRechargeTransaction().getStatus());
		dto.setInitiatedAt(invoice.getRechargeTransaction().getInitiatedAt());
		dto.setCompletedAt(invoice.getRechargeTransaction().getCompletedAt());

		dto.setUserId(invoice.getRechargeTransaction().getUser().getUserId());
		dto.setUserName(invoice.getRechargeTransaction().getUser().getFullName());
		dto.setUserEmail(invoice.getRechargeTransaction().getUser().getEmail());
		dto.setUserMobile(invoice.getRechargeTransaction().getUser().getMobileNumber());

		dto.setConnectionId(invoice.getRechargeTransaction().getConnection().getConnectionId());
		dto.setRechargedMobileNumber(invoice.getRechargeTransaction().getConnection().getMobileNumber());
		dto.setOperatorName(invoice.getRechargeTransaction().getConnection().getOperator().getOperatorName());
		dto.setCircle(invoice.getRechargeTransaction().getConnection().getCircle());

		dto.setPlanId(invoice.getRechargeTransaction().getPlan().getPlanId());
		dto.setPlanName(invoice.getRechargeTransaction().getPlan().getPlanName());
		dto.setValidityDays(invoice.getRechargeTransaction().getPlan().getValidityDays());
		dto.setDataBenefits(invoice.getRechargeTransaction().getPlan().getDataBenefits());
		dto.setCallBenefits(invoice.getRechargeTransaction().getPlan().getCallBenefits());
		dto.setSmsBenefits(invoice.getRechargeTransaction().getPlan().getSmsBenefits());

		dto.setPaymentMethod(payment.getPaymentMethod());
		dto.setTransactionReference(payment.getTransactionReference());
		dto.setAmountPaid(payment.getAmount());

		return dto;
	}
}