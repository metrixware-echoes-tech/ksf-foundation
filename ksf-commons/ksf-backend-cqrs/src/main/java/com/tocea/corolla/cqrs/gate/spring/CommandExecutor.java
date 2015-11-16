package com.tocea.corolla.cqrs.gate.spring;

import java.util.Arrays;

import com.tocea.corolla.cqrs.gate.CommandExecutionException;
import com.tocea.corolla.cqrs.gate.spring.api.ICommandCallback;
import com.tocea.corolla.cqrs.gate.spring.api.ICommandExecutionListener;
import com.tocea.corolla.cqrs.gate.spring.api.ICommandExecutor;

/**
 * This task may be executed asynchronously. She contains all the logic to
 * execute a command and performs several triggers.
 *
 * @author sleroy
 *
 * @param <R>
 */
public class CommandExecutor<R> implements ICommandExecutor<R> {

	private final ICommandCallback<R> callback;

	private final ICommandExecutionListener[]	listeners;
	private final Object						command;

	/**
	 * Instantiates a new command executor.
	 *
	 * @param _command
	 *            the _command
	 * @param _callback
	 *            the _callback
	 * @param _listeners
	 *            the _listeners
	 */
	public CommandExecutor(final Object _command, final ICommandCallback<R> _callback,
			final ICommandExecutionListener[] _listeners) {
		command = _command;
		callback = _callback;
		listeners = _listeners;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public R call() {
		R result = null;
		try {

			result = callback.call();

			// Notify listeners
			notifyListenersSuccess(result);

		} catch (final Exception e) {
			notifyListenersFailure(e);
			throw new CommandExecutionException(command, e);

		}
		return result;
	}

	@Override
	public String toString() {
		return "CommandExecutor [callback=" + callback + ", listeners=" + Arrays.toString(listeners) + ", command="
				+ command + "]";
	}

	/**
	 * Notify listeners failure.
	 *
	 * @param _exception
	 *            the exception
	 */
	private void notifyListenersFailure(final Throwable _exception) {
		for (final ICommandExecutionListener commandExecutionListener : listeners) {
			commandExecutionListener.onFailure(command, _exception);
		}
	}

	/**
	 * Notify listeners success.
	 *
	 * @param result
	 *            the result
	 */
	private void notifyListenersSuccess(final Object result) {
		for (final ICommandExecutionListener commandExecutionListener : listeners) {
			commandExecutionListener.onSuccess(command, result);
		}
	}
}
