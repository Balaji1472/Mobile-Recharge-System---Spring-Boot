package com.mrs.enpoint.feature.planoffer.dto;

import jakarta.validation.constraints.NotBlank;

public class PlanOfferRequestDTO {

	private int offerId;
	
	@NotBlank(message="Enter the priority from 1 to 4")
	private int priority;

	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}