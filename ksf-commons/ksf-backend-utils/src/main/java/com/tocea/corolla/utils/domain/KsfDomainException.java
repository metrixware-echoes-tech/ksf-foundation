/**
 *
 */
package com.tocea.corolla.utils.domain;

/**
 * This exception is a wrapper for exceptions produced inside the domain (pojo,
 * dao, services).
 *
 * @author sleroy
 */
public class KsfDomainException extends RuntimeException {

	private static final long	serialVersionUID	= 5843375436779568216L;
	private static final String	DOMAIN_EXCEPTION	= "Domain Exception : ";

	/**
	 * Domain exception.
	 */
	public KsfDomainException() {
		this("");
	}

	/**
	 * Instantiates a new ksf domain exception.
	 *
	 * @param _message the message
	 */
	public KsfDomainException(final String _message) {
		this(_message, null);
	}

	/**
	 * Instantiates a new ksf domain exception.
	 *
	 * @param _message the _message
	 * @param _cause the _cause
	 */
	public KsfDomainException(final String _message, final Throwable _cause) {
		super(DOMAIN_EXCEPTION + _message, _cause);
	}

}
