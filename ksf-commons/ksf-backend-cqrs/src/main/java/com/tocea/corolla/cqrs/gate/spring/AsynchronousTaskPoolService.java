package com.tocea.corolla.cqrs.gate.spring;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.tocea.corolla.cqrs.gate.conf.CqrsConfiguration;
import com.tocea.corolla.cqrs.gate.spring.api.IAsynchronousTaskPoolService;
import com.tocea.corolla.cqrs.gate.spring.api.ICommandExecutor;

/**
 * The Class AsynchronousTaskPoolService implements a pool of task. The tasks
 * are executed asynchronously and configured with {@link CqrsConfiguration}
 */
@Service
public class AsynchronousTaskPoolService implements IAsynchronousTaskPoolService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsynchronousTaskPoolService.class);

	/**
	 * Creates the callable.
	 *
	 * @param <V>
	 *            the value type
	 * @param _executableTask
	 *            the _executable task
	 * @return the callable
	 */
	public static <V> Callable<V> createCallable(final ICommandExecutor<V> _executableTask) {
		return new Callable<V>() {

			@Override
			public V call() throws Exception {
				return _executableTask.call();
			}
		};
	}

	private ThreadPoolTaskExecutor executorService = null;

	/** The cqrs configuration. */

	private final CqrsConfiguration cqrsConfiguration;

	/**
	 * Instantiates a new asynchronous task pool service.
	 *
	 * @param _cqrsConfiguration
	 *            the _cqrs configuration
	 */
	@Autowired
	public AsynchronousTaskPoolService(final CqrsConfiguration _cqrsConfiguration) {
		super();
		cqrsConfiguration = _cqrsConfiguration;
	}

	/**
	 * Destroy the service
	 */
	@PreDestroy
	public void destroy() {
		LOGGER.info("Destroying the Asynchronous Task Pool");
		executorService.destroy();
	}

	/**
	 * Returns the executor service
	 *
	 * @return the executor service.
	 */
	@Override
	public ThreadPoolTaskExecutor getExecutorService() {
		return executorService;
	}

	/**
	 * Inits the service
	 */
	@PostConstruct
	public void init() {
		LOGGER.info("Initializing the Asynchronous Task Pool");
		executorService = new ThreadPoolTaskExecutor();
		executorService.setCorePoolSize(cqrsConfiguration.getCorePoolSize());
		LOGGER.info("AsynchronousEngine cores : {}", cqrsConfiguration.getCorePoolSize());
		executorService.setKeepAliveSeconds(cqrsConfiguration.getKeepAliveSeconds());
		LOGGER.info("AsynchronousEngine keepAlive : {}", cqrsConfiguration.getKeepAliveSeconds());
		if (cqrsConfiguration.getMaxPoolSize() > 0) {
			LOGGER.info("AsynchronousEngine maxPoolSize : {}", cqrsConfiguration.getMaxPoolSize());
			executorService.setMaxPoolSize(cqrsConfiguration.getMaxPoolSize());

		} else {
			LOGGER.info("AsynchronousEngine pool size : UNLIMITED");
			executorService.setMaxPoolSize(Integer.MAX_VALUE);
		}
		if (cqrsConfiguration.getQueueCapacity() > 0) {
			LOGGER.info("AsynchronousEngine maxPoolSize : {}", cqrsConfiguration.getQueueCapacity());
			executorService.setQueueCapacity(cqrsConfiguration.getQueueCapacity());

		} else {
			LOGGER.info("AsynchronousEngine queue capacity : UNLIMITED");
			executorService.setQueueCapacity(Integer.MAX_VALUE);
		}
		executorService.setThreadGroupName("corolla-command-tasks");
		executorService.setThreadNamePrefix("corolla-command-thread-");
		executorService.setThreadPriority(cqrsConfiguration.getThreadPriority());
		executorService.initialize();
	}

	public void setExecutorService(final ThreadPoolTaskExecutor _executorService) {
		executorService = _executorService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tocea.corolla.cqrs.gate.spring.IAsynchronousTaskPoolService#submit(
	 * java.util.concurrent.Callable)
	 */
	@Override
	public <V> Future<V> submit(final ICommandExecutor<V> _executableTask) {
		LOGGER.info("Submit task {}", _executableTask);
		return executorService.submit(createCallable(_executableTask));

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tocea.corolla.cqrs.gate.spring.IAsynchronousTaskPoolService#submit(
	 * java.util.concurrent.Callable,
	 * org.springframework.util.concurrent.ListenableFutureCallback)
	 */
	@Override
	public <V> ListenableFuture<V> submit(final ICommandExecutor<V> _executableTask,
			final ListenableFutureCallback<V> _callback) {
		LOGGER.info("Submit task {}", _executableTask);
		final ListenableFuture<V> listenableFuture = executorService.submitListenable(createCallable(_executableTask));
		listenableFuture.addCallback(_callback);
		return listenableFuture;

	}
}
