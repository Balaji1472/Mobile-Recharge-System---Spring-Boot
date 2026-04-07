package com.mrs.enpoint.feature.refund.service;

import com.mrs.enpoint.entity.Payment;
import com.mrs.enpoint.entity.Refund;
import com.mrs.enpoint.entity.RechargeTransaction;
import com.mrs.enpoint.feature.auditlog.enums.AuditAction;
import com.mrs.enpoint.feature.auditlog.enums.EntityName;
import com.mrs.enpoint.feature.auditlog.service.AuditService;
import com.mrs.enpoint.feature.notification.service.NotificationService;
import com.mrs.enpoint.feature.payment.repository.PaymentRepository;
import com.mrs.enpoint.feature.refund.dto.RefundResponseDTO;
import com.mrs.enpoint.feature.refund.enums.RefundStatus;
import com.mrs.enpoint.feature.refund.mapper.RefundMapper;
import com.mrs.enpoint.feature.refund.repository.RefundRepository;
import com.mrs.enpoint.feature.recharge.enums.RechargeStatus;
import com.mrs.enpoint.feature.recharge.repository.RechargeTransactionRepository;
import com.mrs.enpoint.shared.exception.NotFoundException;
import com.mrs.enpoint.shared.security.SecurityUtils;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RefundServiceImpl implements RefundService {

	private final RefundRepository refundRepository;
	private final PaymentRepository paymentRepository;
	private final RechargeTransactionRepository rechargeRepository;
	private final NotificationService notificationService;
	private final AuditService auditService;
	private final SecurityUtils securityUtils;

	public RefundServiceImpl(RefundRepository refundRepository, PaymentRepository paymentRepository,
			RechargeTransactionRepository rechargeRepository, NotificationService notificationService,
			AuditService auditService, SecurityUtils securityUtils) {
		this.refundRepository = refundRepository;
		this.paymentRepository = paymentRepository;
		this.rechargeRepository = rechargeRepository;
		this.notificationService = notificationService;
		this.auditService = auditService;
		this.securityUtils = securityUtils;
	}

	/**
	 * Auto-triggered from PaymentServiceImpl when: rechargeStatus = FAILED AND
	 * paymentStatus = SUCCESS i.e. money was deducted but recharge did not go
	 * through.
	 */
	@Override
	@Transactional
	public void processAutoRefund(int paymentId) {

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new NotFoundException("Payment not found with id: " + paymentId));

		RechargeTransaction recharge = payment.getRechargeTransaction();

		// Update recharge status to REFUNDED
		recharge.setStatus(RechargeStatus.REFUNDED);
		rechargeRepository.save(recharge);

		// Create refund record
		Refund refund = new Refund();
		refund.setPayment(payment);
		refund.setAmount(payment.getAmount());
		refund.setStatus(RefundStatus.PROCESSED);
		refund.setProcessedAt(LocalDateTime.now());

		Refund savedRefund = refundRepository.save(refund);

		int userId = recharge.getUser().getUserId();
		String mobileNumber = recharge.getConnection().getMobileNumber();
		String amount = payment.getAmount().toPlainString();

		// Notify user about the refund
		notificationService.sendRefundNotification(userId, mobileNumber, amount);

		// Audit log
		auditService.log(userId, EntityName.REFUND, savedRefund.getRefundId(), AuditAction.REFUND_COMPLETED, null,
				"Refund of Rs." + amount + " processed for connection: " + mobileNumber);
	}

	@Override
	@PreAuthorize("hasRole('USER')")
	public List<RefundResponseDTO> getMyRefunds() {

		int currentUserId = securityUtils.getCurrentUserId();

		List<Refund> refunds = refundRepository.findByPayment_RechargeTransaction_User_UserId(currentUserId);

		if (refunds.isEmpty()) {
			throw new NotFoundException("No refunds found for your account");
		}

		return refunds.stream().map(refund -> RefundMapper.toResponseDTO(refund)).collect(Collectors.toList());
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public List<RefundResponseDTO> getAllRefunds() {

		List<Refund> refunds = refundRepository.findAll();

		if (refunds.isEmpty()) {
			throw new NotFoundException("No refunds found in the system");
		}

		return refunds.stream().map(refund -> RefundMapper.toResponseDTO(refund)).collect(Collectors.toList());
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public RefundResponseDTO getRefundById(int refundId) {

		Refund refund = refundRepository.findById(refundId)
				.orElseThrow(() -> new NotFoundException("Refund not found with id: " + refundId));

		return RefundMapper.toResponseDTO(refund);
	}
}