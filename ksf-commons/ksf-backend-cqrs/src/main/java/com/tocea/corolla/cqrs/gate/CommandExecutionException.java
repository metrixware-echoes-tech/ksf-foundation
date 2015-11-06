package com.tocea.corolla.cqrs.gate;

public class CommandExecutionException extends CqrsException {

	public CommandExecutionException(final Object _command, final Throwable _e) {
		super("Command " + _command + " has failed", _e);
	}

	public CommandExecutionException(final Throwable cause) {
		super(cause);
	}

}
