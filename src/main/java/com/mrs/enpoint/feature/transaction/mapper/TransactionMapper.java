package com.mrs.enpoint.feature.transaction.mapper;

import com.mrs.enpoint.entity.Payment;
import com.mrs.enpoint.entity.RechargeTransaction;
import com.mrs.enpoint.feature.transaction.dto.TransactionResponseDTO;

public class TransactionMapper {

	private TransactionMapper() {
	}

	/**
	 * @param recharge   the recharge entity
	 * @param payment    the latest payment attempt for this recharge
	 * @param userMobile the logged-in user's registered mobile number (for
	 *                   isOwnNumber check)
	 */
	public static TransactionResponseDTO toResponseDTO(RechargeTransaction recharge, Payment payment,
			String userMobile) {
		TransactionResponseDTO dto = new TransactionResponseDTO();

		// Recharge side
		dto.setRechargeId(recharge.getRechargeId());
		dto.setUserId(recharge.getUser().getUserId());
		dto.setFullName(recharge.getUser().getFullName());
		dto.setConnectionId(recharge.getConnection().getConnectionId());
		dto.setMobileNumber(recharge.getConnection().getMobileNumber());
		dto.setOwnNumber(recharge.getConnection().getMobileNumber().equals(userMobile));
		dto.setPlanId(recharge.getPlan().getPlanId());
		dto.setPlanName(recharge.getPlan().getPlanName());
		dto.setFinalAmount(recharge.getFinalAmount());
		dto.setRechargeStatus(recharge.getStatus());
		dto.setInitiatedAt(recharge.getInitiatedAt());
		dto.setCompletedAt(recharge.getCompletedAt());

		// Payment side
		dto.setPaymentId(payment.getPaymentId());
		dto.setPaymentMethod(payment.getPaymentMethod());
		dto.setPaymentStatus(payment.getStatus());
		dto.setTransactionReference(payment.getTransactionReference());
		dto.setAttemptNumber(payment.getAttemptNumber());
		dto.setFailureReason(payment.getFailureReason());
		dto.setPaymentTime(payment.getPaymentTime());

		return dto;
	}
}