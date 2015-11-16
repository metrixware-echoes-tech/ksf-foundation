package com.tocea.corolla.cqrs.gate;

/**
 * The Class CommandExecutionException defines the exception that is thrown when
 * a command has failed.
 */
public class CommandExecutionException extends CqrsException {
	
	private static final long serialVersionUID = -3236092315468247437L;
	
	/**
	 * Instantiates a new command execution exception.
	 *
	 * @param _command the _command
	 * @param _e the _e
	 */
	public CommandExecutionException(final Object _command, final Throwable _e) {
		super("Command " + _command + " has failed", _e);
	}
	
	/**
	 * Instantiates a new command execution exception.
	 *
	 * @param cause the cause
	 */
	public CommandExecutionException(final Throwable cause) {
		super(cause);
	}
	
}
