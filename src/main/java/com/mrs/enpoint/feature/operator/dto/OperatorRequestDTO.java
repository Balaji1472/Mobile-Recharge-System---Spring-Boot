package com.mrs.enpoint.feature.operator.dto;

import com.mrs.enpoint.feature.auth.enums.Status;

public class OperatorRequestDTO {

	private String operatorName;
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