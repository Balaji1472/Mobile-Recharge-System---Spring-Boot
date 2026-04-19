package com.mrs.enpoint.feature.transaction.dto;

import com.mrs.enpoint.feature.payment.enums.PaymentMethod;
import com.mrs.enpoint.feature.payment.enums.PaymentStatus;
import com.mrs.enpoint.feature.recharge.enums.RechargeStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponseDTO {

    // --- Recharge side ---
    private int rechargeId;
    private int userId;
    private String fullName;
    private int connectionId;
    private String mobileNumber;   // the recharged SIM
    private boolean isOwnNumber;   // true = user's own registered mobile
    private int planId;
    private String planName;
    private BigDecimal finalAmount;
    private RechargeStatus rechargeStatus;
    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;

    // --- Payment side ---
    private int paymentId;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionReference;
    private int attemptNumber;
    private String failureReason;
    private LocalDateTime paymentTime;

    public int getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(int rechargeId) {
        this.rechargeId = rechargeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getFullName() {
    	return fullName;
    }
    
    public void setFullName(String fullName) {
    	this.fullName = fullName;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isOwnNumber() {
        return isOwnNumber;
    }

    public void setOwnNumber(boolean ownNumber) {
        isOwnNumber = ownNumber;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public RechargeStatus getRechargeStatus() {
        return rechargeStatus;
    }

    public void setRechargeStatus(RechargeStatus rechargeStatus) {
        this.rechargeStatus = rechargeStatus;
    }

    public LocalDateTime getInitiatedAt() {
        return initiatedAt;
    }

    public void setInitiatedAt(LocalDateTime initiatedAt) {
        this.initiatedAt = initiatedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(int attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }
}