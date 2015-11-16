package com.tocea.corolla.cqrs.gate.spring;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tocea.corolla.cqrs.gate.conf.CqrsConfiguration;

public class AsynchronousTaskPoolServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsynchronousTaskPoolServiceTest.class);

	private AsynchronousTaskPoolService service;

	@After
	public void after() {
		service.destroy();
	}

	@Before
	public void init() {
		service = new AsynchronousTaskPoolService(new CqrsConfiguration());
		service.init();
	}

	/**
	 * Test the capacity to launch several tasks into the pool.
	 */
	@Test
	public void testSubmit() {
		for (int i = 0; i < 100; ++i) {
			try {
				final int number = i;
				final Future<Integer> submit = service.submit(new Callable<Integer>() {

					@Override
					public Integer call() throws Exception {
						LOGGER.info("Task {}", number);
						service.submit(new Callable<Integer>() {
							
							@Override
							public Integer call() throws Exception {
								return 145;
							}
						}).get();
						return number;
					}
				});
				submit.get();

			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

}
