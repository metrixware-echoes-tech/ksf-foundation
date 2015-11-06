package com.tocea.corolla.cqrs.gate.spring;

import com.tocea.corolla.cqrs.gate.spring.api.ICommandCallback;
import com.tocea.corolla.cqrs.handler.ICommandHandler;

/**
 * Default callback to invoke sequentially a command.
 *
 * @author sleroy
 *        
 * @param <R>
 */
public class DefaultCommandCallback<R> implements ICommandCallback<R> {

	private final ICommandHandler<Object, Object>	handler;
	private final Object							command;

	public DefaultCommandCallback(final Object _command, final ICommandHandler<Object, Object> _handler) {
		command = _command;
		handler = _handler;
	}

	@Override
	public R call() throws Exception {

		return (R) handler.handle(command);
	}
	
}
