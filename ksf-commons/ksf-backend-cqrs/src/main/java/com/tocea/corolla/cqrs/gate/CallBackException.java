package com.tocea.corolla.cqrs.gate;

/**
 * This exception is thrown when the callback is failing.
 *
 * @author sleroy
 *
 */
public class CallBackException extends CqrsException {
	
	private static final long serialVersionUID = 2565066461594381413L;
	
	/**
	 * Instantiates a new call back exception.
	 *
	 * @param _tCallBackException the _t call back exception
	 * @param _e the _e
	 */
	public CallBackException(final Throwable _tCallBackException, final Throwable _e) {
		super(_tCallBackException.getMessage(), _e);
	}
	
}
