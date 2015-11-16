package com.tocea.corolla.cqrs.gate.spring;

import java.util.Arrays;
import java.util.concurrent.Callable;

import com.tocea.corolla.cqrs.gate.CommandExecutionException;
import com.tocea.corolla.cqrs.gate.spring.api.ICommandCallback;
import com.tocea.corolla.cqrs.gate.spring.api.ICommandExecutionListener;

/**
 * This task may be executed asynchronously. She contains all the logic to
 * execute a command and performs several triggers.
 *
 * @author sleroy
 *
 * @param <R>
 */
public class CommandExecutor<R> implements Callable<R> {

	private final ICommandCallback<R> callback;

	private final ICommandExecutionListener[]	listeners;
	private final Object						command;

	public CommandExecutor(final Object _command, final ICommandCallback<R> _callback,
			final ICommandExecutionListener[] _listeners) {
		command = _command;
		callback = _callback;
		listeners = _listeners;
	}

	@Override
	public R call() throws Exception {
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

	private void notifyListenersFailure(final Throwable e) {
		for (final ICommandExecutionListener commandExecutionListener : listeners) {
			commandExecutionListener.onFailure(command, e);
		}
	}

	private void notifyListenersSuccess(final Object result) {
		for (final ICommandExecutionListener commandExecutionListener : listeners) {
			commandExecutionListener.onSuccess(command, result);
		}
	}
}
