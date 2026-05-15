package com.mrs.enpoint.feature.payment.service;

import com.mrs.enpoint.entity.Payment;
import com.mrs.enpoint.entity.RechargeTransaction;
import com.mrs.enpoint.feature.auditlog.enums.AuditAction;
import com.mrs.enpoint.feature.auditlog.enums.EntityName;
import com.mrs.enpoint.feature.auditlog.service.AuditService;
import com.mrs.enpoint.feature.auth.repository.UserRepository;
import com.mrs.enpoint.feature.invoice.service.InvoiceService;
import com.mrs.enpoint.feature.notification.service.NotificationService;
import com.mrs.enpoint.feature.payment.dto.PaymentResponseDTO;
import com.mrs.enpoint.feature.payment.dto.PaymentVerifyRequestDTO;
import com.mrs.enpoint.feature.payment.enums.PaymentStatus;
import com.mrs.enpoint.feature.payment.mapper.PaymentMapper;
import com.mrs.enpoint.feature.payment.repository.PaymentRepository;
import com.mrs.enpoint.feature.recharge.enums.RechargeStatus;
import com.mrs.enpoint.feature.recharge.repository.RechargeTransactionRepository;
import com.mrs.enpoint.feature.refund.service.RefundService;
import com.mrs.enpoint.shared.exception.BusinessException;
import com.mrs.enpoint.shared.exception.NotFoundException;
import com.mrs.enpoint.shared.razorpay.RazorpayService;
import com.mrs.enpoint.shared.security.SecurityUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RechargeTransactionRepository rechargeRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final SecurityUtils securityUtils;
    private final RefundService refundService;
    private final RazorpayService razorpayService;
    private final NotificationService notificationService;
    private final InvoiceService invoiceService;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                               RechargeTransactionRepository rechargeRepository,
                               UserRepository userRepository,
                               AuditService auditService,
                               SecurityUtils securityUtils,
                               @Lazy RefundService refundService,
                               RazorpayService razorpayService,
                               NotificationService notificationService,
                               InvoiceService invoiceService) {
        this.paymentRepository = paymentRepository;
        this.rechargeRepository = rechargeRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
        this.securityUtils = securityUtils;
        this.refundService = refundService;
        this.razorpayService = razorpayService;
        this.notificationService = notificationService;
        this.invoiceService = invoiceService;
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public PaymentResponseDTO getPaymentById(int paymentId) {

        int currentUserId = securityUtils.getCurrentUserId();

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found with id: " + paymentId));

        boolean isAdmin = isCurrentUserAdmin();
        if (!isAdmin && payment.getRechargeTransaction().getUser().getUserId() != currentUserId) {
            throw new AccessDeniedException("You are not authorized to view this payment");
        }

        return PaymentMapper.toResponseDTO(payment);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public PaymentResponseDTO verifyAndUpdatePayment(PaymentVerifyRequestDTO request) {

        int currentUserId = securityUtils.getCurrentUserId();

        Payment payment = paymentRepository
                .findByTransactionReference(request.getRazorpayOrderId())
                .orElseThrow(() -> new NotFoundException(
                        "No payment found for Razorpay order: " + request.getRazorpayOrderId()));

        RechargeTransaction recharge = payment.getRechargeTransaction();

        if (recharge.getUser().getUserId() != currentUserId) {
            throw new AccessDeniedException("You are not authorized to verify this payment");
        }

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return PaymentMapper.toResponseDTO(payment);
        }

        boolean isValid = razorpayService.verifyPaymentSignature(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature()
        );

        String mobileNumber = recharge.getConnection().getMobileNumber();
        String planPrice    = recharge.getFinalAmount().toPlainString();

        if (isValid) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setTransactionReference(request.getRazorpayPaymentId());
            payment.setPaymentTime(LocalDateTime.now());
            paymentRepository.save(payment);

            recharge.setStatus(RechargeStatus.SUCCESS);
            recharge.setCompletedAt(LocalDateTime.now());
            rechargeRepository.save(recharge);

            auditService.log(currentUserId, EntityName.RECHARGE,
                    recharge.getRechargeId(), AuditAction.RECHARGE_SUCCESS, null,
                    "Recharge successful for connection: " + mobileNumber
                            + ", plan: " + recharge.getPlan().getPlanName());

            notificationService.sendRechargeNotification(currentUserId, true, mobileNumber, planPrice);
            invoiceService.generateInvoice(recharge.getRechargeId());

        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Signature verification failed — payment tampered or invalid");
            payment.setPaymentTime(LocalDateTime.now());
            paymentRepository.save(payment);

            recharge.setStatus(RechargeStatus.FAILED);
            recharge.setCompletedAt(LocalDateTime.now());
            rechargeRepository.save(recharge);

            auditService.log(currentUserId, EntityName.RECHARGE,
                    recharge.getRechargeId(), AuditAction.RECHARGE_FAILED, null,
                    "Recharge payment signature invalid for connection: " + mobileNumber);

            notificationService.sendRechargeNotification(currentUserId, false, mobileNumber, planPrice);

            throw new BusinessException("Payment verification failed. Invalid signature.");
        }

        return PaymentMapper.toResponseDTO(payment);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public PaymentResponseDTO retryPayment(int rechargeId) {

        int currentUserId = securityUtils.getCurrentUserId();

        RechargeTransaction recharge = rechargeRepository.findById(rechargeId)
                .orElseThrow(() -> new NotFoundException("Recharge not found with id: " + rechargeId));

        if (recharge.getUser().getUserId() != currentUserId) {
            throw new AccessDeniedException("You are not authorized to retry this payment");
        }

        if (recharge.getStatus() != RechargeStatus.FAILED) {
            throw new BusinessException(
                    "Retry is only allowed for FAILED recharges. Current status: " + recharge.getStatus());
        }

        Payment lastPayment = paymentRepository
                .findTopByRechargeTransaction_RechargeIdOrderByAttemptNumberDesc(rechargeId)
                .orElseThrow(() -> new NotFoundException("No payment record found for recharge id: " + rechargeId));

        if (lastPayment.getAttemptNumber() >= 2) {
            throw new BusinessException("Maximum retry attempts (2) exceeded for this recharge.");
        }

        int nextAttempt = lastPayment.getAttemptNumber() + 1;

        Payment retryPayment = new Payment();
        retryPayment.setRechargeTransaction(recharge);
        retryPayment.setPaymentMethod(lastPayment.getPaymentMethod());
        retryPayment.setAmount(recharge.getFinalAmount());
        retryPayment.setAttemptNumber(nextAttempt);
        retryPayment.setPaymentTime(LocalDateTime.now());
        retryPayment.setTransactionReference(
                "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase());
        retryPayment.setStatus(PaymentStatus.SUCCESS);
        Payment savedPayment = paymentRepository.save(retryPayment);

        if (lastPayment.getStatus() == PaymentStatus.SUCCESS
                && recharge.getStatus() == RechargeStatus.FAILED) {
            refundService.processAutoRefund(lastPayment.getPaymentId());
        }

        recharge.setStatus(RechargeStatus.SUCCESS);
        recharge.setCompletedAt(LocalDateTime.now());
        rechargeRepository.save(recharge);

        if (!userRepository.existsById(currentUserId)) {
            throw new NotFoundException("User not found with id: " + currentUserId);
        }

        auditService.log(currentUserId, EntityName.RECHARGE, rechargeId,
                AuditAction.RECHARGE_SUCCESS, "FAILED",
                "Recharge retry successful on attempt " + nextAttempt
                        + " for connection: " + recharge.getConnection().getMobileNumber());

        return PaymentMapper.toResponseDTO(savedPayment);
    }
    
    @Override
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public void cancelPayment(String razorpayOrderId) {
        int currentUserId = securityUtils.getCurrentUserId();

        Payment payment = paymentRepository
                .findByTransactionReference(razorpayOrderId)
                .orElseThrow(() -> new NotFoundException(
                        "No payment found for Razorpay order: " + razorpayOrderId));

        RechargeTransaction recharge = payment.getRechargeTransaction();

        if (recharge.getUser().getUserId() != currentUserId) {
            throw new AccessDeniedException("Not authorized");
        }

        // Idempotency — don't overwrite a SUCCESS
        if (payment.getStatus() == PaymentStatus.SUCCESS) return;

        payment.setStatus(PaymentStatus.FAILED);
        payment.setFailureReason("User dismissed payment without completing");
        payment.setPaymentTime(LocalDateTime.now());
        paymentRepository.save(payment);

        recharge.setStatus(RechargeStatus.FAILED);
        recharge.setCompletedAt(LocalDateTime.now());
        rechargeRepository.save(recharge);

        String mobileNumber = recharge.getConnection().getMobileNumber();
        String planPrice    = recharge.getFinalAmount().toPlainString();

        auditService.log(currentUserId, EntityName.RECHARGE,
                recharge.getRechargeId(), AuditAction.RECHARGE_FAILED, null,
                "User dismissed Razorpay for connection: " + mobileNumber);

        notificationService.sendRechargeNotification(
                currentUserId, false, mobileNumber, planPrice);
    }

    private boolean isCurrentUserAdmin() {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

}