package com.tocea.corolla.cqrs.gate.spring.api;

import com.tocea.corolla.cqrs.gate.CommandExecutionException;

/**
 * The Interface ICommandExecutor defines the method to invoke the command.
 *
 * @param <R>
 *            the generic type for the returned value.
 */
public interface ICommandExecutor<R> {
	
	/**
	 * Invokes the command
	 *
	 * @return the result.
	 */
	R call() throws CommandExecutionException;
}
