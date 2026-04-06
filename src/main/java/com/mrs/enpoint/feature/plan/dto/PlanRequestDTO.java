package com.mrs.enpoint.feature.plan.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class PlanRequestDTO {

	
    private int operatorId;
    private int categoryId;
    
    @NotBlank(message="Plan name is required")
    private String planName;
    
    @Positive(message="enter the positive value greater than 0")
    private BigDecimal price;
    
    private int validityDays;
    private String dataBenefits;
    private String callBenefits;
    private String smsBenefits;

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
}