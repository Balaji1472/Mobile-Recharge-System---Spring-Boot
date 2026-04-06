package com.mrs.enpoint.shared.exception;

public class DuplicateAlreadyExistsException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateAlreadyExistsException(String message) {
		super(message);
	}
}
