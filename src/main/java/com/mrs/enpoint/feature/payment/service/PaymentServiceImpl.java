package com.mrs.enpoint.feature.payment.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mrs.enpoint.entity.Payment;
import com.mrs.enpoint.entity.RechargeTransaction;
//import com.mrs.enpoint.entity.User;
import com.mrs.enpoint.feature.auditlog.enums.AuditAction;
import com.mrs.enpoint.feature.auditlog.enums.EntityName;
import com.mrs.enpoint.feature.auditlog.service.AuditService;
import com.mrs.enpoint.feature.auth.repository.UserRepository;
import com.mrs.enpoint.feature.payment.dto.PaymentResponseDTO;
import com.mrs.enpoint.feature.payment.enums.PaymentStatus;
import com.mrs.enpoint.feature.payment.mapper.PaymentMapper;
import com.mrs.enpoint.feature.payment.repository.PaymentRepository;
import com.mrs.enpoint.feature.recharge.enums.RechargeStatus;
import com.mrs.enpoint.feature.recharge.repository.RechargeTransactionRepository;
import com.mrs.enpoint.shared.exception.BusinessException;
import com.mrs.enpoint.shared.exception.NotFoundException;
import com.mrs.enpoint.shared.security.SecurityUtils;

@Service
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final RechargeTransactionRepository rechargeRepository;
	private final UserRepository userRepository;
	private final AuditService auditService;
	private final SecurityUtils securityUtils;

	public PaymentServiceImpl(PaymentRepository paymentRepository, RechargeTransactionRepository rechargeRepository,
			UserRepository userRepository, AuditService auditService, SecurityUtils securityUtils) {
		this.paymentRepository = paymentRepository;
		this.rechargeRepository = rechargeRepository;
		this.userRepository = userRepository;
		this.auditService = auditService;
		this.securityUtils = securityUtils;
	}

	@Override
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public PaymentResponseDTO getPaymentById(int paymentId) {

		int currentUserId = securityUtils.getCurrentUserId();

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new NotFoundException("Payment not found with id: " + paymentId));

		// user can only view payments linked to their own recharges
		boolean isAdmin = isCurrentUserAdmin();
		if (!isAdmin && payment.getRechargeTransaction().getUser().getUserId() != currentUserId) {
			throw new AccessDeniedException("You are not authorized to view this payment");
		}

		return PaymentMapper.toResponseDTO(payment);
	}

	@Override
	@PreAuthorize("hasRole('USER')")
	@Transactional
	public PaymentResponseDTO retryPayment(int rechargeId) {

		int currentUserId = securityUtils.getCurrentUserId();

		// verify the recharge belongs to the current user
		RechargeTransaction recharge = rechargeRepository.findById(rechargeId)
				.orElseThrow(() -> new NotFoundException("Recharge not found with id: " + rechargeId));

		if (recharge.getUser().getUserId() != currentUserId) {
			System.out.println(recharge.getUser().getFullName());
			throw new AccessDeniedException("You are not authorized to retry this payment");
		}

		// only failed recharges can be retired
		if (recharge.getStatus() != RechargeStatus.FAILED) {
			throw new BusinessException(
					"Retry is only allowed for FAILED recharges. Current status: " + recharge.getStatus());
		}

		// get the last attempt number
		Payment lastPayment = paymentRepository
				.findTopByRechargeTransaction_RechargeIdOrderByAttemptNumberDesc(rechargeId)
				.orElseThrow(() -> new NotFoundException("No payment record found for recharge id: " + rechargeId));

		// If the last attempt was already the 2nd attempt, block further retries
		if (lastPayment.getAttemptNumber() >= 2) {
			throw new BusinessException("Maximum retry attempts (2) exceeded for this recharge.");
		}

		int nextAttempt = lastPayment.getAttemptNumber() + 1;

		// build new payment attempt
		Payment retryPayment = new Payment();
		retryPayment.setRechargeTransaction(recharge);
		retryPayment.setPaymentMethod(lastPayment.getPaymentMethod());
		retryPayment.setAmount(recharge.getFinalAmount());
		retryPayment.setAttemptNumber(nextAttempt);
		retryPayment.setPaymentTime(LocalDateTime.now());
		retryPayment.setTransactionReference(
				"TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase());

		// simulated success on retry - real integration calls the payment gateway
		retryPayment.setStatus(PaymentStatus.SUCCESS);
		Payment savedPayment = paymentRepository.save(retryPayment);

		// update the recharge transaction to success
		recharge.setStatus(RechargeStatus.SUCCESS);
		recharge.setCompletedAt(LocalDateTime.now());
		rechargeRepository.save(recharge);

		if (!userRepository.existsById(currentUserId)) {
			throw new NotFoundException("User not found with id: " + currentUserId);
		}

		auditService.log(currentUserId, EntityName.RECHARGE, rechargeId, AuditAction.RECHARGE_SUCCESS, "FAILED",
				"Recharge retry successful on attempt " + nextAttempt + " for connection: "
						+ recharge.getConnection().getMobileNumber());

		return PaymentMapper.toResponseDTO(savedPayment);
	}

	private boolean isCurrentUserAdmin() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
	}
}
