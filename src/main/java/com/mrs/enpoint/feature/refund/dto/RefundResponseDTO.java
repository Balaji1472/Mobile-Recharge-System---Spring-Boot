package com.mrs.enpoint.feature.refund.dto;

import com.mrs.enpoint.feature.payment.enums.PaymentMethod;
import com.mrs.enpoint.feature.refund.enums.RefundStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RefundResponseDTO {

    private int refundId;
    private int paymentId;
    private int rechargeId;
    private int userId;
    private String fullName;
    private String mobileNumber;        
    private String planName;
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
    private RefundStatus status;
    private LocalDateTime processedAt;

    public int getRefundId() {
        return refundId;
    }

    public void setRefundId(int refundId) {
        this.refundId = refundId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public RefundStatus getStatus() {
        return status;
    }

    public void setStatus(RefundStatus status) {
        this.status = status;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
}