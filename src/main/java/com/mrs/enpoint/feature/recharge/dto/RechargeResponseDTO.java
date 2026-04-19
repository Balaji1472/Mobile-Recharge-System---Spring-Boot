package com.mrs.enpoint.feature.recharge.dto;

import com.mrs.enpoint.feature.recharge.enums.RechargeStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RechargeResponseDTO {

    private int rechargeId;
    private int userId;
    private int connectionId;
    private String mobileNumber;   // the recharged SIM number
    private boolean isOwnNumber;   // true = user's own registered mobile, false = recharged for someone else
    private int planId;
    private String planName;
    private BigDecimal finalAmount;
    private RechargeStatus status;
    private String appliedOfferName;
    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;

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

    public String getAppliedOfferName() {
		return appliedOfferName;
	}

	public void setAppliedOfferName(String appliedOfferName) {
		this.appliedOfferName = appliedOfferName;
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

    public RechargeStatus getStatus() {
        return status;
    }

    public void setStatus(RechargeStatus status) {
        this.status = status;
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
}