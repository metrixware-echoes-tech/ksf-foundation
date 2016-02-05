package fr.echoes.lab.ksf.extensions.events;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class JsonEventListenerExtensionManagerTest {
	
	private static class DemoJsonClass {
		public String	firstName	= "Sylvain";
		public String	lastName	= "Leroy";
	}
	
	@Test
	public final void testListenAnyEvent() throws Exception {
		final IJsonEventListenerExtension extension = mock(IJsonEventListenerExtension.class);
		
		final JsonEventListenerExtensionManager jsonEventListenerExtensionManager = new JsonEventListenerExtensionManager(
				new IJsonEventListenerExtension[] { extension });
		final ArgumentCaptor<JsonEvent> argumentCaptor = ArgumentCaptor.forClass(JsonEvent.class);
		Mockito.doNothing().when(extension).notifyEvent(argumentCaptor.capture());
		jsonEventListenerExtensionManager.listenAnyEvent(new DemoJsonClass());
		assertEquals(1, argumentCaptor.getAllValues().size());
		assertEquals("fr.echoes.lab.ksf.extensions.events.JsonEventListenerExtensionManagerTest$DemoJsonClass",
				argumentCaptor.getValue().getEventName());
		System.out.println(argumentCaptor.getValue().getJsonData());
		assertEquals("{\"firstName\":\"Sylvain\",\"lastName\":\"Leroy\"}",
				argumentCaptor.getValue().getJsonData());
		
	}
	
}
