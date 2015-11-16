package com.tocea.corolla.cqrs.gate.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.validation.constraints.NotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.tocea.corolla.cqrs.gate.CommandExecutionException;
import com.tocea.corolla.cqrs.gate.CommandHandlerNotFoundException;
import com.tocea.corolla.cqrs.gate.InvalidCommandException;
import com.tocea.corolla.cqrs.gate.conf.CqrsConfiguration;
import com.tocea.corolla.cqrs.gate.spring.api.HandlersProvider;
import com.tocea.corolla.cqrs.gate.spring.api.ICommandExecutionListener;
import com.tocea.corolla.cqrs.gate.spring.api.ICommandProfilingService;
import com.tocea.corolla.cqrs.handler.ICommandHandler;

@RunWith(MockitoJUnitRunner.class)
public class CommandExecutorFactoryServiceTest {

	private static class InvalidObject {
		@NotNull
		String str;
	}

	private static final String COMMAND = "SALUT";

	@Mock
	private HandlersProvider handlersProvider;

	private final CqrsConfiguration configuration = new CqrsConfiguration();

	@Mock
	private ICommandProfilingService profilingService;

	@InjectMocks
	private final CommandExecutorFactoryService service = new CommandExecutorFactoryService();

	@Before
	public void before() {
		service.setConfiguration(configuration);
		service.setListeners(new ICommandExecutionListener[0]);
	}

	@Test
	public final void testRun() {
		configuration.setProfilingEnabled(false);
		Mockito.when(handlersProvider.getHandler(COMMAND)).thenReturn(new ICommandHandler<Object, Object>() {

			@Override
			public Object handle(final Object _command) {

				return _command + " LA TERRE";
			}
		});
		assertEquals("SALUT LA TERRE", service.run(COMMAND));
	}

	@Test(expected = InvalidCommandException.class)
	public final void testRun_invalid_command() {
		configuration.setProfilingEnabled(false);
		service.run(new InvalidObject());
	}

	@Test(expected = CommandExecutionException.class)
	public final void testRun_withFailingHandler() {
		configuration.setProfilingEnabled(false);
		final ICommandHandler<Object, Object> handler = new ICommandHandler<Object, Object>() {

			@Override
			public Object handle(final Object _command) {

				throw new UnsupportedOperationException();
			}
		};
		Mockito.when(handlersProvider.getHandler(COMMAND)).thenReturn(handler);
		assertNull(service.run(COMMAND));
	}

	@Test(expected = CommandHandlerNotFoundException.class)
	public final void testRun_without_handler() {
		configuration.setProfilingEnabled(false);
		service.run(COMMAND);
	}
}
