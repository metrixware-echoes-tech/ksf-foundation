package com.tocea.corolla.cqrs.gate.spring;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SpringGateTest {
	private static final String COMMAND = "GNI";

	
	@Mock
	private CommandExecutorFactoryService commandExecutorFactoryService;

	@InjectMocks
	private SpringGate springGate;

	@Test
	public void testDispatch() throws Exception {
		springGate.dispatch(COMMAND);
		verify(commandExecutorFactoryService, Mockito.times(1)).run(COMMAND);
	}
	


}
