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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.tocea.corolla.cqrs.gate.CqrsException;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.gate.IEventBus;
import com.tocea.corolla.cqrs.gate.spring.api.IAsynchronousTaskPoolService;

/**
 * This class defines the gate where the commands are dispatched for execution.
 *
 * @author sleroy
 *
 */
@Service
public class SpringGate implements Gate {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringGate.class);

	@Autowired
	private CommandExecutorFactoryService commandExecutorFactoryService;

	@Autowired
	private IEventBus eventBus;

	@Autowired
	private IAsynchronousTaskPoolService asynchronousTaskPoolService;

	/**
	 * Executes sequentially.
	 *
	 * @param <R> the generic type
	 * @param _command the _command
	 * @return the result of the command
	 */
	@Override
	public <R> R dispatch(final Object _command)  {
		final Future<R> future = dispatchAsync(_command);
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new CqrsException("Error when trying to access to the result of a command", e);
		}

	}
	
	

	/* (non-Javadoc)
	 * @see com.tocea.corolla.cqrs.gate.Gate#dispatchAsync(java.lang.Object)
	 */
	@Override
	public <R> Future<R> dispatchAsync(final Object _command) {
		final Callable<R> callable = commandExecutorFactoryService.run(_command);
		return asynchronousTaskPoolService.submit(callable);
	}

	/* (non-Javadoc)
	 * @see com.tocea.corolla.cqrs.gate.Gate#dispatchAsync(java.lang.Object, org.springframework.util.concurrent.ListenableFutureCallback)
	 */
	@Override
	public <R> ListenableFuture<R> dispatchAsync(final Object _command, final ListenableFutureCallback<R> _callback) {
		final Callable<R> callable = commandExecutorFactoryService.run(_command);
		return asynchronousTaskPoolService.submit(callable, _callback);
	}

	/* (non-Javadoc)
	 * @see com.tocea.corolla.cqrs.gate.Gate#dispatchEvent(java.lang.Object)
	 */
	@Override
	public void dispatchEvent(final Object _event) {
		LOGGER.trace("Received event {}", _event);
		eventBus.dispatchEvent(_event);
	}

}
