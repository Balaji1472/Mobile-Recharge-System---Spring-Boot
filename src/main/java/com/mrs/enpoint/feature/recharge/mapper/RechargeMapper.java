package com.mrs.enpoint.feature.recharge.mapper;

import com.mrs.enpoint.entity.RechargeTransaction;
import com.mrs.enpoint.feature.recharge.dto.RechargeResponseDTO;

public class RechargeMapper {

	private RechargeMapper() {
	}

	/**
	 * @param recharge   the recharge entity
	 * @param userMobile the logged-in user's registered mobile number
	 */
	public static RechargeResponseDTO toResponseDTO(RechargeTransaction recharge, String userMobile) {
		RechargeResponseDTO dto = new RechargeResponseDTO();
		dto.setRechargeId(recharge.getRechargeId());
		dto.setUserId(recharge.getUser().getUserId());
		dto.setConnectionId(recharge.getConnection().getConnectionId());
		dto.setMobileNumber(recharge.getConnection().getMobileNumber());
		// isOwnNumber = true when the recharged SIM belongs to user's own registered
		// mobile
		dto.setOwnNumber(recharge.getConnection().getMobileNumber().equals(userMobile));
		dto.setPlanId(recharge.getPlan().getPlanId());
		dto.setPlanName(recharge.getPlan().getPlanName());
		dto.setFinalAmount(recharge.getFinalAmount());
		dto.setStatus(recharge.getStatus());
		dto.setInitiatedAt(recharge.getInitiatedAt());
		dto.setCompletedAt(recharge.getCompletedAt());
		return dto;
	}
}