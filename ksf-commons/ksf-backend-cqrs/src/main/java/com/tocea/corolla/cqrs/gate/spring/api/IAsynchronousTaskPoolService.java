package com.tocea.corolla.cqrs.gate.spring.api;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * The Interface IAsynchronousTaskPoolService defines a service that executes
 * tasks by submitting them to a pool. In the pool there are threads that shares
 * the heap of tasks. Tasks are executed asynchronously.
 */
public interface IAsynchronousTaskPoolService {
	
	/**
	 * Submits a new task to the pool.
	 *
	 * @param _executableTask
	 *            the callable task
	 * @return a future object designated the future result
	 */
	<V> Future<V> submit(Callable<V> _executableTask);
	
	/**
	 * Submits a new task to the pool.
	 *
	 * @param <V> the value type
	 * @param _executableTask            the callable task
	 * @param _callback the _callback
	 * @return a future object designated the future result
	 */
	<V> ListenableFuture<V> submit(Callable<V> _executableTask, ListenableFutureCallback<V> _callback);
	
}