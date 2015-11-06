/**
 *
 */
package com.tocea.corolla.utils.domain;

/**
 * This exception is a wrapper for exceptions produced inside the domain (pojo, dao, services)
 * @author sleroy
 *
 */
public class CorollaDomainException extends RuntimeException {

	/**
	 *
	 */
	private static final String	DOMAIN_EXCEPTION	= "Domain Exception : ";

	/**
	 * Domain exception.
	 */
	public CorollaDomainException() {
		this("");
	}

	/**
	 * @param _message
	 */
	public CorollaDomainException(final String _message) {
		this(_message, null);
	}

	/**
	 *
	 * @param _message
	 * @param _cause
	 */
	public CorollaDomainException(final String _message, final Throwable _cause) {
		super(DOMAIN_EXCEPTION + _message, _cause);
	}




}
