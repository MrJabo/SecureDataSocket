package com.cryptolib;

public class SecureDataSocketException extends Exception {

	private Exception originalException;
	private boolean critical = false;

	SecureDataSocketException(String description, Exception originalException) {
		this(description, originalException, true);	
	}

	SecureDataSocketException(String description, Exception originalException, boolean critical) {
		super(description);
		this.critical = critical;
		this.originalException = originalException;
	}
}
