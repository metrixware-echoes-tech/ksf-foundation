package com.tocea.corolla.cqrs.gate.spring.api;

/**
 * This interface defines the listener on
 * 
 * @author sleroy
 *         
 */
public interface ICommandExecutionListener {
	
	/** Invoked when command handling execution resulted in an error. */
	void onFailure(Object _command, Throwable cause);
	
	/** Invoked when command handling execution was successful. */
	void onSuccess(Object _command, Object result);
}