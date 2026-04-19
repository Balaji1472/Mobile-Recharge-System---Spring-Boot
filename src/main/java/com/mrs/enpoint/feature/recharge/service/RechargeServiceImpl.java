package com.mrs.enpoint.feature.recharge.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mrs.enpoint.entity.MobileConnection;
import com.mrs.enpoint.entity.Offer;
import com.mrs.enpoint.entity.Payment;
import com.mrs.enpoint.entity.Plan;
import com.mrs.enpoint.entity.PlanOffer;
import com.mrs.enpoint.entity.RechargeTransaction;
import com.mrs.enpoint.entity.User;
import com.mrs.enpoint.feature.auditlog.enums.AuditAction;
import com.mrs.enpoint.feature.auditlog.enums.EntityName;
import com.mrs.enpoint.feature.auditlog.service.AuditService;
import com.mrs.enpoint.feature.auth.repository.UserRepository;
import com.mrs.enpoint.feature.invoice.service.InvoiceService;
import com.mrs.enpoint.feature.notification.service.NotificationService;
import com.mrs.enpoint.feature.offer.enums.DiscountType;
import com.mrs.enpoint.feature.payment.enums.PaymentStatus;
import com.mrs.enpoint.feature.payment.repository.PaymentRepository;
import com.mrs.enpoint.feature.plan.dto.PlanResponseDTO;
import com.mrs.enpoint.feature.plan.mapper.PlanMapper;
import com.mrs.enpoint.feature.plan.repository.PlanRepository;
import com.mrs.enpoint.feature.planoffer.repository.PlanOfferRepository;
import com.mrs.enpoint.feature.recharge.dto.RechargeRequestDTO;
import com.mrs.enpoint.feature.recharge.dto.RechargeResponseDTO;
import com.mrs.enpoint.feature.recharge.enums.ConnectionStatus;
import com.mrs.enpoint.feature.recharge.enums.RechargeStatus;
import com.mrs.enpoint.feature.recharge.mapper.RechargeMapper;
import com.mrs.enpoint.feature.recharge.repository.MobileConnectionRepository;
import com.mrs.enpoint.feature.recharge.repository.RechargeTransactionRepository;
import com.mrs.enpoint.shared.exception.BusinessException;
import com.mrs.enpoint.shared.exception.NotFoundException;
import com.mrs.enpoint.shared.security.SecurityUtils;

@Service
public class RechargeServiceImpl implements RechargeService {

	private final RechargeTransactionRepository rechargeRepository;
	private final MobileConnectionRepository connectionRepository;
	private final PlanRepository planRepository;
	private final PlanOfferRepository planOfferRepository;
	private final PaymentRepository paymentRepository;
	private final UserRepository userRepository;
	private final AuditService auditService;
	private final SecurityUtils securityUtils;
	private final NotificationService notificationService;
	private final InvoiceService invoiceService;

	public RechargeServiceImpl(RechargeTransactionRepository rechargeRepository,
			MobileConnectionRepository connectionRepository, PlanRepository planRepository,
			PlanOfferRepository planOfferRepository, PaymentRepository paymentRepository, UserRepository userRepository,
			AuditService auditService, SecurityUtils securityUtils, NotificationService notificationService,
			InvoiceService invoiceService) {
		this.rechargeRepository = rechargeRepository;
		this.connectionRepository = connectionRepository;
		this.planRepository = planRepository;
		this.planOfferRepository = planOfferRepository;
		this.paymentRepository = paymentRepository;
		this.userRepository = userRepository;
		this.auditService = auditService;
		this.securityUtils = securityUtils;
		this.notificationService = notificationService;
		this.invoiceService = invoiceService;
	}

	@Override
	@PreAuthorize("hasRole('USER')")
	@Transactional
	public RechargeResponseDTO initiateRecharge(RechargeRequestDTO request) {

		int currentUserId = securityUtils.getCurrentUserId();

		User user = userRepository.findById(currentUserId)
				.orElseThrow(() -> new NotFoundException("Logged-in user not found"));

		// validate connection exists and is active
		MobileConnection connection = connectionRepository.findById(request.getConnectionId()).orElseThrow(
				() -> new NotFoundException("Connection not found with this id: " + request.getConnectionId()));

		if (connection.getStatus() != ConnectionStatus.ACTIVE) {
			throw new BusinessException("Connection with id " + request.getConnectionId() + " is not active");
		}

		// validate plan exists and is active
		Plan plan = planRepository.findById(request.getPlanId())
				.orElseThrow(() -> new NotFoundException("Plan not found with id: " + request.getPlanId()));

		if (!plan.getIsActive()) {
			throw new BusinessException("Plan with id " + request.getPlanId() + " is not active");
		}

		// operator mismatch check - the selected plan must belong to appropriate
		// operator
		if (plan.getOperator().getOperatorId() != connection.getOperator().getOperatorId()) {
			throw new BusinessException("Operator mismatch: the selected plan belongs to "
					+ plan.getOperator().getOperatorName() + " but the connection belongs to "
					+ connection.getOperator().getOperatorName() + ". Please choose a plan from the correct operator.");
		}
		
		BigDecimal basePrice = plan.getPrice();
		BigDecimal finalAmount = BigDecimal.ZERO;
		String appliedOfferName = "None";
		
		//fetch offer for this plan, if available
		List<PlanOffer> planOffers = planOfferRepository.findByPlan_PlanId(plan.getPlanId());
		
		if(!planOffers.isEmpty()) {
			//pick the offer with highest priority
			PlanOffer bestOffer = planOffers.stream()
				    .min(Comparator.comparingInt(p -> p.getPriority()))
				    .orElse(null);
	
			if(bestOffer != null) {
				Offer offer = bestOffer.getOffer();
				// calculate discount properly with two diff types
				appliedOfferName = offer.getTitle();
	            BigDecimal discountValue = offer.getDiscountValue();
	            
	            if(offer.getDiscountType() == DiscountType.PERCENTAGE) {
	            	BigDecimal discountAmount = basePrice.multiply(discountValue).divide(BigDecimal.valueOf(100));
	            	finalAmount = basePrice.subtract(discountAmount);
	            }
	            else if(offer.getDiscountType() == DiscountType.FLAT) {
	            	finalAmount = basePrice.subtract(discountValue);
	            }
	            
	            // final price doesn't go below zero
	            if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
	                finalAmount = BigDecimal.ZERO;
	            }
			}
		}
		
		// build recharge transaction with PENDING status
		RechargeTransaction recharge = new RechargeTransaction();
		recharge.setUser(user);
		recharge.setConnection(connection);
		recharge.setPlan(plan);
		recharge.setFinalAmount(finalAmount);
		recharge.setStatus(RechargeStatus.PENDING);
		recharge.setInitiatedAt(LocalDateTime.now());

		RechargeTransaction savedRecharge = rechargeRepository.save(recharge);

		// simulate payment processing
		Payment payment = new Payment();
		payment.setRechargeTransaction(savedRecharge);
		payment.setPaymentMethod(request.getPaymentMethod());
		payment.setAmount(plan.getPrice());
		payment.setAttemptNumber(1);
		payment.setPaymentTime(LocalDateTime.now());
		payment.setTransactionReference(
				"TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase());

		// simulated success 
		boolean paymentSuccess = true;

		if (paymentSuccess) {

			payment.setStatus(PaymentStatus.SUCCESS);
			paymentRepository.save(payment);

			savedRecharge.setStatus(RechargeStatus.SUCCESS);
			savedRecharge.setCompletedAt(LocalDateTime.now());
			rechargeRepository.save(savedRecharge);

			// audit log — success
			auditService.log(currentUserId, EntityName.RECHARGE, savedRecharge.getRechargeId(),
					AuditAction.RECHARGE_SUCCESS, null, "Recharge successful for connection: "
							+ connection.getMobileNumber() + ", plan: " + plan.getPlanName());

			// send success notification
			notificationService.sendRechargeNotification(currentUserId, true, connection.getMobileNumber(),
					plan.getPrice().toPlainString());

			// generate invoice — only on success
			invoiceService.generateInvoice(savedRecharge.getRechargeId());

		} else {

			payment.setStatus(PaymentStatus.FAILED);
			payment.setFailureReason("Payment gateway declined the transaction");
			paymentRepository.save(payment);

			savedRecharge.setStatus(RechargeStatus.FAILED);
			savedRecharge.setCompletedAt(LocalDateTime.now());
			rechargeRepository.save(savedRecharge);

			// audit log — failure
			auditService.log(currentUserId, EntityName.RECHARGE, savedRecharge.getRechargeId(),
					AuditAction.RECHARGE_FAILED, null, "Recharge failed for connection: " + connection.getMobileNumber()
							+ ", plan: " + plan.getPlanName());

			// send failure notification — no invoice generated
			notificationService.sendRechargeNotification(currentUserId, false, connection.getMobileNumber(),
					plan.getPrice().toPlainString());
		}

		return RechargeMapper.toResponseDTO(savedRecharge, user.getMobileNumber(), appliedOfferName);
	}

	@Override
	@PreAuthorize("hasRole('USER')")
	public List<PlanResponseDTO> getPlansForMobileNumber(String mobileNumber) {

		MobileConnection conn = connectionRepository.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new NotFoundException("No connection found for mobile number: " + mobileNumber));

		if (conn.getStatus() != ConnectionStatus.ACTIVE) {
			throw new BusinessException("The connection for " + mobileNumber + " is currently inactive.");
		}

		int operatorId = conn.getOperator().getOperatorId();

		List<Plan> plans = planRepository.findByOperator_OperatorId(operatorId);

		if (plans.isEmpty()) {
			throw new NotFoundException("No active plans found for operator: " + conn.getOperator().getOperatorName());
		}

		return plans.stream().filter(plan -> plan.getIsActive()).map(plan -> PlanMapper.toResponseDTO(plan))
				.collect(Collectors.toList());
	}

	@Override
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public RechargeResponseDTO getRechargeById(int rechargeId) {

		int currentUserId = securityUtils.getCurrentUserId();
		RechargeTransaction recharge = rechargeRepository.findById(rechargeId)
				.orElseThrow(() -> new NotFoundException("Recharge not found with id: " + rechargeId));

		// a user can only view thier own recharge: Admin can view any
		boolean isAdmin = isCurrentUserAdmin();
		if (!isAdmin && recharge.getUser().getUserId() != currentUserId) {
			throw new BusinessException("You are not authorized to view this recharge");
		}

		User user = userRepository.findById(recharge.getUser().getUserId())
				.orElseThrow(() -> new NotFoundException("User not found"));

		return RechargeMapper.toResponseDTO(recharge, user.getMobileNumber(), "");
	}

	@Override
	@PreAuthorize("hasRole('USER')")
	public List<RechargeResponseDTO> getMyRecharges() {

		int currentUserId = securityUtils.getCurrentUserId();

		User user = userRepository.findById(currentUserId)
				.orElseThrow(() -> new NotFoundException("Logged-in user not found"));

		List<RechargeTransaction> recharges = rechargeRepository.findByUser_UserId(currentUserId);

		if (recharges.isEmpty()) {
			throw new NotFoundException("No recharges found for your account");
		}

		return recharges.stream().map(recharge -> RechargeMapper.toResponseDTO(recharge, user.getMobileNumber(), ""))
				.collect(Collectors.toList());
	}

	// helper - checks if the current principal holds admin role
	private boolean isCurrentUserAdmin() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
	}

}
