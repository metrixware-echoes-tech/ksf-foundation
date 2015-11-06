package com.tocea.corolla.cqrs.gate.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.tocea.corolla.cqrs.gate.conf.CorollaCqrsConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class CommandLoggingServiceTest {

	@Mock
	private final CorollaCqrsConfiguration mock = new CorollaCqrsConfiguration();

	@InjectMocks
	private final CommandLoggingService service = new CommandLoggingService();
	
	@Test
	public void testOnFailure() throws Exception {
		service.onFailure("gni", null);
	}
	
	@Test
	public void testOnSuccess() throws Exception {
		service.onSuccess("GNA", "NI");
	}
	
}
