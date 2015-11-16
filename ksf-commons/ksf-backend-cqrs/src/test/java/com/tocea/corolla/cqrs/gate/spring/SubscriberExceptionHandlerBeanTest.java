package com.tocea.corolla.cqrs.gate.spring;

import org.junit.Test;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class SubscriberExceptionHandlerBeanTest {
	
	public static class FakeEventHandler
	{
		
		@Subscribe
		public void suscribe(final String event) {
			throw new RuntimeException();
		}
		
	}
	
	private final SubscriberExceptionHandlerBean errorHandler = new SubscriberExceptionHandlerBean();
	
	/**
	 * Test handle exception.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testHandleException() throws Exception {
		final EventBus eventBus = new EventBus(errorHandler);
		eventBus.register(new FakeEventHandler());
		eventBus.post("GNI");
	}
	
}
