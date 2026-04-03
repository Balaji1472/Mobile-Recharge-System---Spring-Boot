package com.mrs.enpoint.feature.offer.exception;

public class OfferDoesNotExistException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OfferDoesNotExistException(String message) {
		super(message);
	}
}
