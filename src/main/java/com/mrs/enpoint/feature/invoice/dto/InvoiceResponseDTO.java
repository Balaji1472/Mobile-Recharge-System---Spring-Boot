package com.mrs.enpoint.feature.invoice.dto;

import com.mrs.enpoint.feature.payment.enums.PaymentMethod;
import com.mrs.enpoint.feature.recharge.enums.RechargeStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InvoiceResponseDTO {

    // Invoice info
    private int invoiceId;
    private LocalDateTime generatedAt;

    // Recharge info
    private int rechargeId;
    private RechargeStatus rechargeStatus;
    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;

    // User info
    private int userId;
    private String userName;
    private String userEmail;
    private String userMobile;      // user's registered mobile

    // Connection info
    private int connectionId;
    private String rechargedMobileNumber;   // the SIM that was recharged
    private String operatorName;
    private String circle;

    // Plan info
    private int planId;
    private String planName;
    private int validityDays;
    private String dataBenefits;
    private String callBenefits;
    private String smsBenefits;

    // Payment info
    private PaymentMethod paymentMethod;
    private String transactionReference;
    private BigDecimal amountPaid;

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public int getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(int rechargeId) {
        this.rechargeId = rechargeId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public String getRechargedMobileNumber() {
        return rechargedMobileNumber;
    }

    public void setRechargedMobileNumber(String rechargedMobileNumber) {
        this.rechargedMobileNumber = rechargedMobileNumber;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
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

    public int getValidityDays() {
        return validityDays;
    }

    public void setValidityDays(int validityDays) {
        this.validityDays = validityDays;
    }

    public String getDataBenefits() {
        return dataBenefits;
    }

    public void setDataBenefits(String dataBenefits) {
        this.dataBenefits = dataBenefits;
    }

    public String getCallBenefits() {
        return callBenefits;
    }

    public void setCallBenefits(String callBenefits) {
        this.callBenefits = callBenefits;
    }

    public String getSmsBenefits() {
        return smsBenefits;
    }

    public void setSmsBenefits(String smsBenefits) {
        this.smsBenefits = smsBenefits;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }
}