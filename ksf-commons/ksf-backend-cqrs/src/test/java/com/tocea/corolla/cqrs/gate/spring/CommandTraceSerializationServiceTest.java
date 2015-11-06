package com.tocea.corolla.cqrs.gate.spring;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.tocea.corolla.cqrs.gate.conf.CorollaCqrsConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class CommandTraceSerializationServiceTest {
	
	static class FakeCommand {
		private String field;
		
		public FakeCommand(final String _field) {
			super();
			field = _field;
		}
		
		public String getField() {
			return field;
		}
		
		public void setField(final String _field) {
			field = _field;
		}
	}
	
	@Test
	public void testInit() throws Exception {
		final CorollaCqrsConfiguration cqrsConfiguration = new CorollaCqrsConfiguration();
		cqrsConfiguration.setTraceFile(File.createTempFile("trace", "json"));
		final CommandTraceSerializationService commandTraceSerializationService = new CommandTraceSerializationService(
				cqrsConfiguration);
		commandTraceSerializationService.init();
	}
	
	@Test
	public void testOnFailure() throws Exception {
		final CorollaCqrsConfiguration cqrsConfiguration = new CorollaCqrsConfiguration();
		cqrsConfiguration.setTraceFile(File.createTempFile("trace", "json"));
		final CommandTraceSerializationService commandTraceSerializationService = new CommandTraceSerializationService(
				cqrsConfiguration);
		commandTraceSerializationService.init();
		commandTraceSerializationService.onFailure(new FakeCommand("FIELDA"), new UnsupportedOperationException());
	}
	
	@Test
	public void testOnSuccess() throws Exception {
		final CorollaCqrsConfiguration cqrsConfiguration = new CorollaCqrsConfiguration();
		cqrsConfiguration.setTraceFile(File.createTempFile("trace", "json"));
		final CommandTraceSerializationService commandTraceSerializationService = new CommandTraceSerializationService(
				cqrsConfiguration);
		commandTraceSerializationService.init();
		commandTraceSerializationService.onSuccess(new FakeCommand("FIELDA"), "RESULT");
	}
	
}
