package com.mrs.enpoint.feature.analytics.dto;

import com.mrs.enpoint.feature.auth.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UserAnalyticsDTO {

    private long totalRecharges;        
    private long successfulRecharges;   
    private long failedRecharges;       

    private String currentPlanName;
    private String operatorName;
    private BigDecimal amountPaid;

    private LocalDate validUntil;

    private String dataRemaining;
    private String callBenefits;
    private String smsBenefits;

    private Status accountStatus;

    public long getTotalRecharges() {
        return totalRecharges;
    }

    public void setTotalRecharges(long totalRecharges) {
        this.totalRecharges = totalRecharges;
    }

    public long getSuccessfulRecharges() {
        return successfulRecharges;
    }

    public void setSuccessfulRecharges(long successfulRecharges) {
        this.successfulRecharges = successfulRecharges;
    }

    public long getFailedRecharges() {
        return failedRecharges;
    }

    public void setFailedRecharges(long failedRecharges) {
        this.failedRecharges = failedRecharges;
    }

    public String getCurrentPlanName() {
        return currentPlanName;
    }

    public void setCurrentPlanName(String currentPlanName) {
        this.currentPlanName = currentPlanName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public String getDataRemaining() {
        return dataRemaining;
    }

    public void setDataRemaining(String dataRemaining) {
        this.dataRemaining = dataRemaining;
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

    public Status getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Status accountStatus) {
        this.accountStatus = accountStatus;
    }
}