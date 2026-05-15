package com.mrs.enpoint.feature.recharge.dto;

import jakarta.validation.constraints.NotBlank;

public class QuickFormRechargeRequestDTO {

	@NotBlank
	private String mobileNumber;
	
	@NotBlank
	private String operatorName;

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	
	
}
