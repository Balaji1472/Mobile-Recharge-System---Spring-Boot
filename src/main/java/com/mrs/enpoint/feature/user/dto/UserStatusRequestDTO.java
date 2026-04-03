package com.mrs.enpoint.feature.user.dto;

import com.mrs.enpoint.feature.auth.enums.Status;
import jakarta.validation.constraints.NotNull;

public class UserStatusRequestDTO {

	@NotNull(message = "Status is required")
	private Status status;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}