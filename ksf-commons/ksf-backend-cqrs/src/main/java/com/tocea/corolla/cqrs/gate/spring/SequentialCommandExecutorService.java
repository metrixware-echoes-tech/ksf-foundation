/*
 * Copyright 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *
 */
package com.tocea.corolla.cqrs.gate.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tocea.corolla.cqrs.gate.CommandExecutionException;
import com.tocea.corolla.cqrs.gate.CommandHandlerNotFoundException;
import com.tocea.corolla.cqrs.gate.InvalidCommandException;
import com.tocea.corolla.cqrs.gate.conf.CorollaCqrsConfiguration;
import com.tocea.corolla.cqrs.gate.spring.api.HandlersProvider;
import com.tocea.corolla.cqrs.gate.spring.api.ICommandCallback;
import com.tocea.corolla.cqrs.gate.spring.api.ICommandExecutionListener;
import com.tocea.corolla.cqrs.gate.spring.api.ICommandProfilingService;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.utils.domain.ObjectValidation;

/**
 * This class prepares the command to be executed. It can override the default
 * command handler with a wrapper with enhanced functionalities. It Executes
 * SEQUENTIALLY the commands.
 *
 * @author Slawek
 */
@Service
public class SequentialCommandExecutorService {
	
	@Autowired
	private HandlersProvider handlersProvider;
	
	@Autowired
	private ICommandExecutionListener[] listeners;
	
	@Autowired
	private CorollaCqrsConfiguration configuration;
	
	@Autowired
	private ICommandProfilingService profilingService;
	
	public SequentialCommandExecutorService() {
		super();
	}

	public SequentialCommandExecutorService(final CorollaCqrsConfiguration _configuration) {
		configuration = _configuration;
	}

	/**
	 * Executes a command in synchronous way.
	 *
	 * @param command
	 *            the command
	 * @return
	 */
	public <R> R run(final Object command) {
		final ObjectValidation objectValidation = new ObjectValidation();
		if (!objectValidation.isValid(command)) {
			throw new InvalidCommandException(command);
		}
		
		final ICommandHandler<Object, Object> handler = handlersProvider.getHandler(command);
		if (handler == null) {
			throw new CommandHandlerNotFoundException(command);
		}
		// You can add Your own capabilities here: dependency injection,
		// security, transaction management, logging, profiling, spying,
		// storing
		// commands, etc
		
		ICommandCallback<R> callback = new DefaultCommandCallback<R>(command, handler);
		// Decorate with profiling
		if (configuration.isProfilingEnabled()) {
			callback = profilingService.decorate(command, callback);
		}
		R result = null;
		try {
			
			result = callback.call();
			
			// Notify listeners
			notifyListenersSuccess(command, result);
			
		} catch (final Exception e) {
			notifyListenersFailure(command, e);
			throw new CommandExecutionException(command, e);
			
		}
		return result;
	}
	
	public void setConfiguration(final CorollaCqrsConfiguration _configuration) {
		configuration = _configuration;
	}
	
	public void setListeners(final ICommandExecutionListener[] _listeners) {
		listeners = _listeners;
	}
	
	private void notifyListenersFailure(final Object command, final Throwable e) {
		for (final ICommandExecutionListener commandExecutionListener : listeners) {
			commandExecutionListener.onFailure(command, e);
		}
	}
	
	private void notifyListenersSuccess(final Object command, final Object result) {
		for (final ICommandExecutionListener commandExecutionListener : listeners) {
			commandExecutionListener.onSuccess(command, result);
		}
	}
}