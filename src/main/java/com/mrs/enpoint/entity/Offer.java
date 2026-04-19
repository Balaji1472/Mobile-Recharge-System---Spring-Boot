package com.mrs.enpoint.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mrs.enpoint.feature.offer.enums.DiscountType;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "offer")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int offerId;

    @NotBlank
    @Column(name="title", nullable=false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name="discount_type")
    private DiscountType discountType;

    @Column(name="discount_value")
    private BigDecimal discountValue;

    @Column(name="start_date")
    private LocalDate startDate;

    @Column(name="end_date")
    private LocalDate endDate;

    @Column(name="is_active")
    private Boolean isActive = true;

    public Offer() {
    	
    }
    
	public Offer(@NotBlank String title, DiscountType discountType, @NotBlank BigDecimal discountValue, LocalDate startDate,
			LocalDate endDate, Boolean isActive) {
		super();
		this.title = title;
		this.discountType = discountType;
		this.discountValue = discountValue;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isActive = isActive;
	}

	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
   
}