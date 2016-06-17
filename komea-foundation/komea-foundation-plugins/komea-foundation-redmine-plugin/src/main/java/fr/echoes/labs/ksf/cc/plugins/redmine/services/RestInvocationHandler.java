package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tocea.corolla.cqrs.gate.IEventBus;

/**
 * The Class RestInvocationHandler defines an function that catch exceptions and lets the user to add a custom behaviour.
 *
 * @param <F>
 *            the generic type
 * @param <T>
 *            the generic type
 */
public abstract class RestInvocationHandler<F, T> {

	private static final Logger	LOGGER	= LoggerFactory.getLogger(RestInvocationHandler.class);

	private final IEventBus		eventBus;

	public RestInvocationHandler(final IEventBus eventBus) {
		this.eventBus = eventBus;
	}

	/**
	 * Apply.
	 *
	 * @return the t
	 */
	public T call() throws Exception {
		return this.call(null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	public T call(final F input) throws Exception {
		final StopWatch stopWatch = new StopWatch();
		try {

			stopWatch.start();
			LOGGER.trace("Invoking a rest API");
			return this.execute(input);
		} catch (final Exception e) {
			LOGGER.error("Caught an exception when calling a REST API");
			this.eventBus.dispatchErrorEvent(e);
			this.customUserExceptionhandling(e);
		} finally {
			stopWatch.stop();
			LOGGER.trace("REST API Executed {}", stopWatch.toString());

			//
		}
		return null;
	}

	public void customUserExceptionhandling(final Exception e) throws Exception {
		//
	}

	/**
	 * Execute the rest call.
	 *
	 * @param input
	 *            the input
	 * @return the value return by the execution if any.
	 */
	public abstract T execute(final F input) throws Exception;

}
