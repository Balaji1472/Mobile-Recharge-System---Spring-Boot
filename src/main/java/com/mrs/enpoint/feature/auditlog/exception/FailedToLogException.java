package com.mrs.enpoint.feature.auditlog.exception;

public class FailedToLogException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FailedToLogException(String message) {
		super(message);
	}
}
