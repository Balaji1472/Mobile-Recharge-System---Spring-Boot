package com.mrs.enpoint.feature.recharge.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.mrs.enpoint.feature.notification.service.NotificationService;
import com.mrs.enpoint.feature.offer.enums.DiscountType;
import com.mrs.enpoint.feature.payment.enums.PaymentStatus;
import com.mrs.enpoint.feature.payment.repository.PaymentRepository;
import com.mrs.enpoint.feature.plan.dto.PlanResponseDTO;
import com.mrs.enpoint.feature.plan.mapper.PlanMapper;
import com.mrs.enpoint.feature.plan.repository.PlanRepository;
import com.mrs.enpoint.feature.planoffer.repository.PlanOfferRepository;
import com.mrs.enpoint.feature.recharge.dto.QuickFormRechargeRequestDTO;
import com.mrs.enpoint.feature.recharge.dto.RechargeRequestDTO;
import com.mrs.enpoint.feature.recharge.dto.RechargeResponseDTO;
import com.mrs.enpoint.feature.recharge.enums.ConnectionStatus;
import com.mrs.enpoint.feature.recharge.enums.RechargeStatus;
import com.mrs.enpoint.feature.recharge.mapper.RechargeMapper;
import com.mrs.enpoint.feature.recharge.repository.MobileConnectionRepository;
import com.mrs.enpoint.feature.recharge.repository.RechargeTransactionRepository;
import com.mrs.enpoint.shared.exception.BusinessException;
import com.mrs.enpoint.shared.exception.NotFoundException;
import com.mrs.enpoint.shared.razorpay.RazorpayService;
import com.mrs.enpoint.shared.security.SecurityUtils;

@Service
public class RechargeServiceImpl implements RechargeService {

    private static final Logger log = LoggerFactory.getLogger(RechargeServiceImpl.class);

    private final RechargeTransactionRepository rechargeRepository;
    private final MobileConnectionRepository connectionRepository;
    private final PlanRepository planRepository;
    private final PlanOfferRepository planOfferRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final SecurityUtils securityUtils;
    private final NotificationService notificationService;
    private final RazorpayService razorpayService;

    public RechargeServiceImpl(RechargeTransactionRepository rechargeRepository,
            MobileConnectionRepository connectionRepository,
            PlanRepository planRepository,
            PlanOfferRepository planOfferRepository,
            PaymentRepository paymentRepository,
            UserRepository userRepository,
            AuditService auditService,
            SecurityUtils securityUtils,
            NotificationService notificationService,
            RazorpayService razorpayService) {
        this.rechargeRepository = rechargeRepository;
        this.connectionRepository = connectionRepository;
        this.planRepository = planRepository;
        this.planOfferRepository = planOfferRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
        this.securityUtils = securityUtils;
        this.notificationService = notificationService;
        this.razorpayService = razorpayService;
    }


    @Override
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public RechargeResponseDTO initiateRecharge(RechargeRequestDTO request) {

        int currentUserId = securityUtils.getCurrentUserId();

        log.info("Initiating recharge userId={} connectionId={} planId={}",
                currentUserId, request.getConnectionId(), request.getPlanId());

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("Logged-in user not found"));

        MobileConnection connection = connectionRepository
                .findById(request.getConnectionId())
                .orElseThrow(() -> new NotFoundException(
                        "Connection not found with this id: " + request.getConnectionId()));

        if (connection.getStatus() != ConnectionStatus.ACTIVE) {
            log.warn("Recharge blocked: connectionId={} has status={}",
                    request.getConnectionId(), connection.getStatus());
            throw new BusinessException(
                    "Connection with id " + request.getConnectionId() + " is not active");
        }

        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new NotFoundException(
                        "Plan not found with id: " + request.getPlanId()));

        if (!plan.getIsActive()) {
            log.warn("Recharge blocked: planId={} is inactive", request.getPlanId());
            throw new BusinessException(
                    "Plan with id " + request.getPlanId() + " is not active");
        }

        if (plan.getOperator().getOperatorId() != connection.getOperator().getOperatorId()) {
            log.warn("Operator mismatch: planOperator='{}' connectionOperator='{}' userId={}",
                    plan.getOperator().getOperatorName(),
                    connection.getOperator().getOperatorName(),
                    currentUserId);
            throw new BusinessException(
                    "Operator mismatch: the selected plan belongs to "
                    + plan.getOperator().getOperatorName()
                    + " but the connection belongs to "
                    + connection.getOperator().getOperatorName()
                    + ". Please choose a plan from the correct operator.");
        }

        BigDecimal basePrice = plan.getPrice();
        BigDecimal finalAmount = basePrice;
        String appliedOfferName = "None";

        List<PlanOffer> planOffers = planOfferRepository.findByPlan_PlanId(plan.getPlanId());

        if (!planOffers.isEmpty()) {
            PlanOffer bestOffer = planOffers.stream()
                    .min(Comparator.comparingInt(p -> p.getPriority()))
                    .orElse(null);

            if (bestOffer != null) {
                Offer offer = bestOffer.getOffer();
                appliedOfferName = offer.getTitle();
                BigDecimal discountValue = offer.getDiscountValue();

                if (offer.getDiscountType() == DiscountType.PERCENTAGE) {
                    BigDecimal discountAmount = basePrice
                            .multiply(discountValue)
                            .divide(BigDecimal.valueOf(100));
                    finalAmount = basePrice.subtract(discountAmount);
                } else if (offer.getDiscountType() == DiscountType.FLAT) {
                    finalAmount = basePrice.subtract(discountValue);
                }

                if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
                    finalAmount = BigDecimal.ZERO;
                }

                log.debug("Offer '{}' applied planId={} base={} discountType={} final={}",
                        appliedOfferName, plan.getPlanId(), basePrice,
                        offer.getDiscountType(), finalAmount);
            }
        }

        RechargeTransaction recharge = new RechargeTransaction();
        recharge.setUser(user);
        recharge.setConnection(connection);
        recharge.setPlan(plan);
        recharge.setFinalAmount(finalAmount);
        recharge.setStatus(RechargeStatus.PENDING);
        recharge.setInitiatedAt(LocalDateTime.now());

        RechargeTransaction savedRecharge = rechargeRepository.save(recharge);

        BigDecimal amountForOrder = finalAmount.compareTo(BigDecimal.ZERO) > 0
                ? finalAmount
                : basePrice;

        String razorpayOrderId;
        try {
            razorpayOrderId = razorpayService.createOrder(
                    amountForOrder,
                    String.valueOf(savedRecharge.getRechargeId())
            );
            log.info("Razorpay order created orderId='{}' rechargeId={} amount={}",
                    razorpayOrderId, savedRecharge.getRechargeId(), amountForOrder);
        } catch (Exception e) {
            savedRecharge.setStatus(RechargeStatus.FAILED);
            savedRecharge.setCompletedAt(LocalDateTime.now());
            rechargeRepository.save(savedRecharge);

            log.error("Razorpay order creation failed rechargeId={} mobile={} reason={}",
                    savedRecharge.getRechargeId(), connection.getMobileNumber(), e.getMessage(), e);

            auditService.log(currentUserId, EntityName.RECHARGE,
                    savedRecharge.getRechargeId(), AuditAction.RECHARGE_FAILED,
                    null, "Razorpay order creation failed for connection: "
                            + connection.getMobileNumber()
                            + " | Reason: " + e.getMessage());

            notificationService.sendRechargeNotification(currentUserId, false,
                    connection.getMobileNumber(), amountForOrder.toPlainString());

            throw new BusinessException(
                    "Payment gateway error: could not create order. " + e.getMessage());
        }

        Payment payment = new Payment();
        payment.setRechargeTransaction(savedRecharge);
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setAmount(finalAmount);
        payment.setAttemptNumber(1);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setTransactionReference(razorpayOrderId);
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);

        log.info("Recharge pending rechargeId={} mobile='{}' amount={} orderId='{}'",
                savedRecharge.getRechargeId(), connection.getMobileNumber(),
                finalAmount, razorpayOrderId);

        RechargeResponseDTO response = RechargeMapper.toResponseDTO(
                savedRecharge, user.getMobileNumber(), appliedOfferName);
        response.setRazorpayOrderId(razorpayOrderId);
        return response;
    }


    @Override
    @PreAuthorize("hasRole('USER')")
    public int getConnectionIdFromMobile(String mobileNumber) {
        MobileConnection conn = connectionRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new NotFoundException(
                        "No connection found for mobile number: " + mobileNumber));

        if (conn.getStatus() != ConnectionStatus.ACTIVE) {
            log.warn("Connection lookup failed: mobile='{}' status={}",
                    mobileNumber, conn.getStatus());
            throw new BusinessException(
                    "The connection for " + mobileNumber + " is currently inactive.");
        }

        return conn.getConnectionId();
    }


    @Override
    public List<PlanResponseDTO> getPlansForMobileNumber(String mobileNumber) {

        MobileConnection conn = connectionRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new NotFoundException(
                        "No connection found for mobile number: " + mobileNumber));

        if (conn.getStatus() != ConnectionStatus.ACTIVE) {
            throw new BusinessException(
                    "The connection for " + mobileNumber + " is currently inactive.");
        }

        int operatorId = conn.getOperator().getOperatorId();
        List<Plan> plans = planRepository.findByOperator_OperatorId(operatorId);

        if (plans.isEmpty()) {
            log.warn("No active plans found operatorId={} operatorName='{}' mobile='{}'",
                    operatorId, conn.getOperator().getOperatorName(), mobileNumber);
            throw new NotFoundException(
                    "No active plans found for operator: " + conn.getOperator().getOperatorName());
        }

        return plans.stream()
                .filter(plan -> plan.getIsActive())
                .map(plan -> PlanMapper.toResponseDTO(plan))
                .collect(Collectors.toList());
    }


    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public RechargeResponseDTO getRechargeById(int rechargeId) {

        int currentUserId = securityUtils.getCurrentUserId();

        RechargeTransaction recharge = rechargeRepository.findById(rechargeId)
                .orElseThrow(() -> new NotFoundException(
                        "Recharge not found with id: " + rechargeId));

        boolean isAdmin = isCurrentUserAdmin();
        if (!isAdmin && recharge.getUser().getUserId() != currentUserId) {
            log.warn("Unauthorized recharge view attempt userId={} rechargeId={}",
                    currentUserId, rechargeId);
            throw new BusinessException(
                    "You are not authorized to view this recharge");
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

        List<RechargeTransaction> recharges =
                rechargeRepository.findByUser_UserId(currentUserId);

        if (recharges.isEmpty()) {
            throw new NotFoundException("No recharges found for your account");
        }

        return recharges.stream()
                .map(recharge -> RechargeMapper.toResponseDTO(
                        recharge, user.getMobileNumber(), ""))
                .collect(Collectors.toList());
    }

    public boolean validateQuickRecharge(QuickFormRechargeRequestDTO request) {
        int connectionId = getConnectionIdFromMobile(request.getMobileNumber());

        MobileConnection conn = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new NotFoundException("Connection ID mismatch"));

        if (!conn.getOperator().getOperatorName().equalsIgnoreCase(request.getOperatorName())) {
            log.warn("Quick recharge operator mismatch mobile='{}' given='{}' actual='{}'",
                    request.getMobileNumber(), request.getOperatorName(),
                    conn.getOperator().getOperatorName());
            throw new BusinessException(
                    "Selected operator does not match the registered operator for this number.");
        }

        log.info("Quick recharge validated mobile='{}' operator='{}'",
                request.getMobileNumber(), request.getOperatorName());

        return true;
    }


    private boolean isCurrentUserAdmin() {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}