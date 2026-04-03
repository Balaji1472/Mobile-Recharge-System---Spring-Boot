package com.mrs.enpoint.feature.planoffer.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mrs.enpoint.feature.offer.enums.DiscountType;

public class PlanOfferResponseDTO {

    private int planOfferId;
    private int planId;
    private String planName;
    private int offerId;
    private String offerTitle;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean offerIsActive;
    private int priority;

    public int getPlanOfferId() {
        return planOfferId;
    }

    public void setPlanOfferId(int planOfferId) {
        this.planOfferId = planOfferId;
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

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getOfferIsActive() {
        return offerIsActive;
    }

    public void setOfferIsActive(Boolean offerIsActive) {
        this.offerIsActive = offerIsActive;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}