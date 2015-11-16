package com.tocea.corolla.cqrs.gate;

/**
 * The Class CommandHandlerNotFoundException is thrown when a command handler is
 * not defined for a command.
 */
public class CommandHandlerNotFoundException extends CqrsException {
	
	private static final long serialVersionUID = -2502739262120923826L;
	
	/**
	 * Instantiates a new command handler not found exception.
	 *
	 * @param _command
	 *            the _command
	 */
	public CommandHandlerNotFoundException(final Object _command) {
		super("Could not execute the command : " + _command);
	}
}
