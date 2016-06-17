package fr.echoes.labs.ksf.extensions.events;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.cc.api.extensions.events.IJsonEventListenerExtension;
import fr.echoes.labs.ksf.cc.api.extensions.events.JsonEvent;
import fr.echoes.labs.ksf.cc.extmanager.events.JsonEventListenerExtensionManager;
import fr.echoes.labs.pluginfwk.api.extension.ExtensionManager;

public class JsonEventListenerExtensionManagerTest {

	private static class DemoJsonClass {

		public String	firstName	= "Sylvain";
		public String	lastName	= "Leroy";
	}

	@Test
	public final void testListenAnyEvent() throws Exception {
		final IJsonEventListenerExtension extension = mock(IJsonEventListenerExtension.class);

		// Mock ExtensionManager
		final ExtensionManager extensionManager = mock(ExtensionManager.class);
		Mockito.when(extensionManager.findExtensions(IJsonEventListenerExtension.class)).thenReturn(Lists.newArrayList(extension));

		final JsonEventListenerExtensionManager jsonEventListenerExtensionManager = new JsonEventListenerExtensionManager(extensionManager);

		final ArgumentCaptor<JsonEvent> argumentCaptor = ArgumentCaptor.forClass(JsonEvent.class);
		Mockito.doNothing().when(extension).notifyEvent(argumentCaptor.capture());
		jsonEventListenerExtensionManager.listenAnyEvent(new DemoJsonClass());
		assertEquals(1, argumentCaptor.getAllValues().size());
		assertEquals("fr.echoes.labs.ksf.extensions.events.JsonEventListenerExtensionManagerTest$DemoJsonClass", argumentCaptor.getValue().getEventName());
		System.out.println(argumentCaptor.getValue().getJsonData());
		assertEquals("{\"firstName\":\"Sylvain\",\"lastName\":\"Leroy\"}", argumentCaptor.getValue().getJsonData());

	}

}
