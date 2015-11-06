package com.tocea.corolla.cqrs.gate;

/**
 * This exception is thrown when the callback is failing.
 * 
 * @author sleroy
 *        
 */
public class CallBackException extends CqrsException {

	public CallBackException(final Throwable _tCallBackException, final Throwable _e) {
		super(_tCallBackException.getMessage(), _e);
	}

}
