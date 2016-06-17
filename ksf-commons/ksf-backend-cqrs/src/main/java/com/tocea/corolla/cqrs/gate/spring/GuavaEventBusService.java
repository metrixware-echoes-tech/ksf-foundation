package com.tocea.corolla.cqrs.gate.spring;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PreDestroy;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.tocea.corolla.cqrs.annotations.EventHandler;
import com.tocea.corolla.cqrs.gate.IEventBus;
import com.tocea.corolla.cqrs.gate.conf.CqrsConfiguration;
import com.tocea.corolla.cqrs.gate.spring.api.IAsynchronousTaskPoolService;

import fr.echoes.labs.ksf.cc.errormgt.events.ErrorEvent;

/**
 * The Class GuavaEventBusService defines the bus that handles events.
 */
@Service
public class GuavaEventBusService implements ApplicationContextAware, IEventBus {

	private static final Logger					LOGGER	= LoggerFactory.getLogger(GuavaEventBusService.class);
	private EventBus							eventBus;
	private ApplicationContext					applicationContext;

	private final IAsynchronousTaskPoolService	asynchronoustaskService;

	private final CqrsConfiguration				corollaCqrsConfiguation;

	private final SubscriberExceptionHandler	subscriberExceptionHandler;

	/**
	 * Instantiates a new guava event bus service.
	 *
	 * @param _asynchronoustaskService
	 *            the _asynchronoustask service
	 * @param _corollaCqrsConfiguation
	 *            the _corolla cqrs configuation
	 * @param _subscriberExceptionHandler
	 *            the _subscriber exception handler
	 */
	@Autowired
	public GuavaEventBusService(final IAsynchronousTaskPoolService _asynchronoustaskService, final CqrsConfiguration _corollaCqrsConfiguation,
			final SubscriberExceptionHandler _subscriberExceptionHandler) {
		super();
		this.asynchronoustaskService = _asynchronoustaskService;
		this.corollaCqrsConfiguation = _corollaCqrsConfiguation;
		this.subscriberExceptionHandler = _subscriberExceptionHandler;

	}

	/**
	 * Destroy the event bus
	 */
	@PreDestroy
	public void destroyEventBus() {
		this.eventBus = null;

	}

	/*
	 * (non-Javadoc)
	 * @see com.tocea.corolla.cqrs.gate.IEventBus#dispatchErrorEvent(java.lang.Exception)
	 */
	@Override
	public void dispatchErrorEvent(final Exception e) {
		Validate.notNull(e);
		this.dispatchEvent(new ErrorEvent(e));
	}

	@Override
	public void dispatchEvent(final Object _event) {
		this.eventBus.post(_event);
	}

	@Override
	public void setApplicationContext(final ApplicationContext _applicationContext) throws BeansException {
		this.applicationContext = _applicationContext;
		if (this.corollaCqrsConfiguation.isAsyncEventQueries()) {

			this.eventBus = new AsyncEventBus(this.asynchronoustaskService.getExecutorService());
		} else {
			this.eventBus = new EventBus();
		}
		LOGGER.info("Registering the event handlers...");
		final Map<String, Object> beansWithAnnotation = this.applicationContext.getBeansWithAnnotation(EventHandler.class);
		for (final Entry<String, Object> beanEntry : beansWithAnnotation.entrySet()) {
			final String beanId = beanEntry.getKey();
			LOGGER.info("Event handler ---> {}", beanId);
			final Object bean = beanEntry.getValue();
			this.eventBus.register(bean);

		}

	}

}
