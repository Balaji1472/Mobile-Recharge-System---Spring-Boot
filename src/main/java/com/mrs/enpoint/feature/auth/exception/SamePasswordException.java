package com.mrs.enpoint.feature.auth.exception;

public class SamePasswordException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SamePasswordException(String message) {
        super(message);
    }
}