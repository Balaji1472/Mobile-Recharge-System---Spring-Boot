package com.mrs.enpoint.feature.plan.dto;

import java.math.BigDecimal;

public class PlanResponseDTO {

    private int planId;
    private int operatorId;
    private String operatorName;
    private int categoryId;
    private String categoryName;
    private String planName;
    private BigDecimal price;
    private Integer validityDays;
    private String dataBenefits;
    private String callBenefits;
    private String smsBenefits;
    private Boolean isActive;
 
    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public Integer getValidityDays() {
        return validityDays;
    }

    public void setValidityDays(Integer validityDays) {
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}