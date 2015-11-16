package com.tocea.corolla.cqrs.gate.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

/**
 * The Class SubscriberExceptionHandlerBean describes the handler that notifies
 * when an event handler has failed.
 */
@Component
public class SubscriberExceptionHandlerBean implements SubscriberExceptionHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SubscriberExceptionHandlerBean.class);
	
	/* (non-Javadoc)
	 * @see com.google.common.eventbus.SubscriberExceptionHandler#handleException(java.lang.Throwable, com.google.common.eventbus.SubscriberExceptionContext)
	 */
	@Override
	public void handleException(final Throwable _arg0, final SubscriberExceptionContext _arg1) {
		LOGGER.warn("Event has triggered an exception inside an handler {}", _arg0);
		LOGGER.warn("Exception : {}" , _arg0);
		LOGGER.warn("Event : {}" , _arg1.getEvent());
		LOGGER.warn("Suscriber : {}" , _arg1.getSubscriber());
		LOGGER.warn("EventBus : {}" , _arg1.getEventBus());
		LOGGER.warn("SuscriberMethod : {}" , _arg1.getSubscriberMethod());
		
	}
	
}
