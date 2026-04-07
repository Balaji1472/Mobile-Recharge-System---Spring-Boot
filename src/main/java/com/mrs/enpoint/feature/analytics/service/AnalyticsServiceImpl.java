package com.mrs.enpoint.feature.analytics.service;

import com.mrs.enpoint.entity.RechargeTransaction;
import com.mrs.enpoint.entity.User;
import com.mrs.enpoint.feature.analytics.dto.AdminAnalyticsDTO;
import com.mrs.enpoint.feature.analytics.dto.UserAnalyticsDTO;
import com.mrs.enpoint.feature.auth.repository.UserRepository;
import com.mrs.enpoint.feature.plan.repository.PlanRepository;
import com.mrs.enpoint.feature.recharge.enums.RechargeStatus;
import com.mrs.enpoint.feature.recharge.repository.RechargeTransactionRepository;
import com.mrs.enpoint.feature.refund.enums.RefundStatus;
import com.mrs.enpoint.feature.refund.repository.RefundRepository;
import com.mrs.enpoint.shared.exception.NotFoundException;
import com.mrs.enpoint.shared.security.SecurityUtils;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

	private final RechargeTransactionRepository rechargeRepository;
	private final UserRepository userRepository;
	private final PlanRepository planRepository;
	private final RefundRepository refundRepository;
	private final SecurityUtils securityUtils;

	public AnalyticsServiceImpl(RechargeTransactionRepository rechargeRepository, UserRepository userRepository,
			PlanRepository planRepository, RefundRepository refundRepository, SecurityUtils securityUtils) {
		this.rechargeRepository = rechargeRepository;
		this.userRepository = userRepository;
		this.planRepository = planRepository;
		this.refundRepository = refundRepository;
		this.securityUtils = securityUtils;
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public AdminAnalyticsDTO getAdminOverview() {

		AdminAnalyticsDTO dto = new AdminAnalyticsDTO();

		// Total recharges across platform
		dto.setTotalRecharges(rechargeRepository.count());

		// Breakdown by status
		dto.setSuccessfulRecharges(rechargeRepository.countByStatus(RechargeStatus.SUCCESS));
		dto.setFailedRecharges(rechargeRepository.countByStatus(RechargeStatus.FAILED));
		dto.setRefundedRecharges(rechargeRepository.countByStatus(RechargeStatus.REFUNDED));

		// Total revenue — sum of final_amount for SUCCESS recharges
		BigDecimal revenue = rechargeRepository.sumSuccessfulRechargeAmounts();
		dto.setTotalRevenue(revenue != null ? revenue : BigDecimal.ZERO);

		// Total registered users
		dto.setTotalUsers(userRepository.count());

		// Active plans
		dto.setActivePlans(planRepository.findAll().stream().filter(p -> Boolean.TRUE.equals(p.getIsActive())).count());

		// Refund stats
		dto.setTotalRefundsProcessed(refundRepository.countByStatus(RefundStatus.PROCESSED));
		BigDecimal refundAmount = refundRepository.sumProcessedRefundAmounts();
		dto.setTotalRefundAmount(refundAmount != null ? refundAmount : BigDecimal.ZERO);

		return dto;
	}

	@Override
	@PreAuthorize("hasRole('USER')")
	public UserAnalyticsDTO getUserOverview() {

		int currentUserId = securityUtils.getCurrentUserId();

		User user = userRepository.findById(currentUserId)
				.orElseThrow(() -> new NotFoundException("Logged-in user not found"));

		UserAnalyticsDTO dto = new UserAnalyticsDTO();

		// Total recharges by this user
		dto.setTotalRecharges(rechargeRepository.countByUser_UserIdAndStatus(currentUserId, RechargeStatus.SUCCESS)
				+ rechargeRepository.countByUser_UserIdAndStatus(currentUserId, RechargeStatus.FAILED)
				+ rechargeRepository.countByUser_UserIdAndStatus(currentUserId, RechargeStatus.REFUNDED)
				+ rechargeRepository.countByUser_UserIdAndStatus(currentUserId, RechargeStatus.PENDING));

		dto.setSuccessfulRecharges(
				rechargeRepository.countByUser_UserIdAndStatus(currentUserId, RechargeStatus.SUCCESS));

		dto.setFailedRecharges(rechargeRepository.countByUser_UserIdAndStatus(currentUserId, RechargeStatus.FAILED));

		// Account status
		dto.setAccountStatus(user.getStatus());

		// Current active plan — derived from latest SUCCESS recharge
		List<RechargeTransaction> latestSuccess = rechargeRepository.findLatestSuccessRechargeByUser(currentUserId);

		if (!latestSuccess.isEmpty()) {
			RechargeTransaction latest = latestSuccess.get(0);

			dto.setCurrentPlanName(latest.getPlan().getPlanName());
			dto.setOperatorName(latest.getPlan().getOperator().getOperatorName());
			dto.setAmountPaid(latest.getFinalAmount());

			// validUntil = completedAt date + validityDays
			LocalDate validUntil = latest.getCompletedAt().toLocalDate().plusDays(latest.getPlan().getValidityDays());
			dto.setValidUntil(validUntil);

			// Data benefits taken directly from plan
			dto.setDataRemaining(latest.getPlan().getDataBenefits());
			dto.setCallBenefits(latest.getPlan().getCallBenefits());
			dto.setSmsBenefits(latest.getPlan().getSmsBenefits());

		} else {
			// No successful recharge yet — plan fields are null
			dto.setCurrentPlanName(null);
			dto.setOperatorName(null);
			dto.setAmountPaid(null);
			dto.setValidUntil(null);
			dto.setDataRemaining(null);
			dto.setCallBenefits(null);
			dto.setSmsBenefits(null);
		}

		return dto;
	}
}