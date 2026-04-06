package com.mrs.enpoint.feature.operator.dto;

import com.mrs.enpoint.feature.auth.enums.Status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OperatorRequestDTO {

	@NotBlank(message="Operator name is required")
	private String operatorName;
	
	@NotNull(message="operator status is required('ACTIVE', 'INACTIVE')")
	private Status status;

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}