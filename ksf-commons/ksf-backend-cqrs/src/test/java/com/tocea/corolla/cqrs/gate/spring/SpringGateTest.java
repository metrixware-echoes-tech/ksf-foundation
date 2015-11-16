package com.tocea.corolla.cqrs.gate.spring;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.tocea.corolla.cqrs.gate.CommandExecutionException;
import com.tocea.corolla.cqrs.gate.spring.api.IAsynchronousTaskPoolService;
import com.tocea.corolla.cqrs.gate.spring.api.ICommandExecutor;

@RunWith(MockitoJUnitRunner.class)
public class SpringGateTest {
	private static final String COMMAND = "GNI";
	
	@Mock
	private CommandExecutorFactoryService commandExecutorFactoryService;
	
	@InjectMocks
	private SpringGate springGate;
	
	@Mock
	private IAsynchronousTaskPoolService taskService;
	
	@Test
	public void testDispatch() throws Exception {
		when(commandExecutorFactoryService.run(COMMAND)).thenReturn(new ICommandExecutor<Object>() {
			
			@Override
			public Object call() throws CommandExecutionException {
				return "GNI";
			}
		});
		final Object dispatch = springGate.dispatch(COMMAND);
		assertEquals("GNI", dispatch);
		verify(commandExecutorFactoryService, Mockito.times(1)).run(COMMAND);
	}
	
}
