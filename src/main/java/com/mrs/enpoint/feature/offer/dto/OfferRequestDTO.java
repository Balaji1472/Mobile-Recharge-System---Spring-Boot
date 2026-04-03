package com.mrs.enpoint.feature.offer.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mrs.enpoint.feature.offer.enums.DiscountType;

public class OfferRequestDTO {

	private String title;
	private DiscountType discountType;
	private BigDecimal discountValue;
	private LocalDate startDate;
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
