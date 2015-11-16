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
package com.tocea.corolla.cqrs.gate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Main access point to the Application.<br>
 * It handles:
 * <ul>
 * <li>filtering command duplicates
 * <li>command queues for asynchronous commands
 * </ul>
 *
 * @author Slawek
 * @author sleroy
 *
 */
public interface Gate {

	/**
	 * Dispatch a command and executes it asynchronously. The code awaits the
	 * response to return to the result in a sequentialy way.
	 *
	 * @param <R> the generic type
	 * @param command            the command.
	 * @return the result of the command.
	 * @throws InterruptedException the interrupted exception
	 * @throws ExecutionException the execution exception
	 */
	<R> R dispatch(Object command) ;
	
	/**
	 * Dispatch a command and executes it asynchronously since we don't expect
	 * results from it. Warning : Command are not supposed to store results
	 * value.
	 *
	 * @param <R> the generic type
	 * @param command            the command.
	 * @return the result of the command.
	 */
	<R> Future<R> dispatchAsync(Object command);
	
	/**
	 * Dispatch a command and executes it asynchronously since we don't expect
	 * results from it. The returned value is not returned but handled with a
	 * callback that will be triggered at the task completion.
	 *
	 * @param <R> the generic type
	 * @param command            the command.
	 * @param _callback the _callback
	 * @return the result of the command.
	 */
	<R> ListenableFuture<R> dispatchAsync(Object command, ListenableFutureCallback<R> _callback);
	
	/**
	 * Dispatches an event and executes it asynchronously.
	 *
	 * @param _event
	 *            the event.
	 * @deprecated use {@link IEventBus} instead
	 */
	@Deprecated
	void dispatchEvent(Object _event);
}