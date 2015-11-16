/**
 *
 */
package com.tocea.corolla.cqrs.gate;

import com.tocea.corolla.utils.domain.KsfDomainException;

/**
 * This exception is a wrapper for exceptions produced inside the domain (pojo,
 * dao, services).
 *
 * @author sleroy
 */
public class CqrsException extends KsfDomainException {

	private static final long	serialVersionUID	= -208491937717509793L;
	private static final String	DOMAIN_EXCEPTION	= "CQRS Exception : ";

	/**
	 * Domain exception.
	 */
	public CqrsException() {
		this("");
	}

	/**
	 * Instantiates a new cqrs exception.
	 *
	 * @param _message
	 *            the _message
	 */
	public CqrsException(final String _message) {
		this(_message, null);
	}

	/**
	 * Instantiates a new cqrs exception.
	 *
	 * @param _message
	 *            the _message
	 * @param _cause
	 *            the _cause
	 */
	public CqrsException(final String _message, final Throwable _cause) {
		super(DOMAIN_EXCEPTION + _message, _cause);
	}

	/**
	 * Instantiates a new cqrs exception.
	 *
	 * @param cause
	 *            the cause
	 */
	public CqrsException(final Throwable cause) {
		this(cause.getMessage(), cause);
	}

}
