package com.mrs.enpoint.feature.operator.dto;

import com.mrs.enpoint.feature.auth.enums.Status;

public class OperatorResponseDTO {

	private int operatorId;
	private String operatorName;
	private int totalPlans;
	private Status status;

	public int getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}

	public int getTotalPlans() {
		return totalPlans;
	}

	public void setTotalPlans(int totalPlans) {
		this.totalPlans = totalPlans;
	}

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