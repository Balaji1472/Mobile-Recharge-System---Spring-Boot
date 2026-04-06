package com.mrs.enpoint.feature.offer.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mrs.enpoint.feature.offer.enums.DiscountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OfferRequestDTO {

	@NotBlank(message="Enter the title, don't leave it as blank")
	private String title;
	
	@NotNull(message="Enter the discount type: ('FLAT', 'PERCENTAGE')")
	private DiscountType discountType;
	
	@Positive(message="value must be greater than 0")
	private BigDecimal discountValue;
	
	@NotNull(message="enter the valid start date")
	private LocalDate startDate;
	
	@NotNull(message="enter the valid end date")
	private LocalDate endDate;

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

}
