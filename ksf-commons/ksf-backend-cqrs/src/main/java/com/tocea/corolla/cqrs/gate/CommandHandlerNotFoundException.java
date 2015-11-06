package com.tocea.corolla.cqrs.gate;

public class CommandHandlerNotFoundException extends CqrsException {

	public CommandHandlerNotFoundException(final Object _command) {
		super("Could not execute the command : " + _command);
	}
}
