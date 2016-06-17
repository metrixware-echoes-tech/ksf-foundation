package fr.echoes.labs.ksf.cc.extmanager.events;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.Subscribe;
import com.tocea.corolla.cqrs.annotations.EventHandler;

import fr.echoes.labs.ksf.cc.api.extensions.events.IJsonEventListenerExtension;
import fr.echoes.labs.ksf.cc.api.extensions.events.JsonEvent;
import fr.echoes.labs.pluginfwk.api.extension.ExtensionManager;

/**
 * This extension manager dispatch events encapsulated into an handy wrapper.
 *
 * @author sleroy
 *
 */
@EventHandler
public class JsonEventListenerExtensionManager {

	private static final Logger		LOGGER			= LoggerFactory.getLogger(JsonEventListenerExtensionManager.class);

	private final ObjectMapper		objectMapper	= new ObjectMapper();

	private final ExtensionManager	extensionManager;

	@Autowired
	public JsonEventListenerExtensionManager(final ExtensionManager extensionManager) {
		this.extensionManager = extensionManager;
	}

	/**
	 * Listen to any event and dispatch them to the interested plugins.
	 *
	 * @param _object
	 *            the event object
	 */
	@Subscribe
	public void listenAnyEvent(final Object _object) {

		final List<IJsonEventListenerExtension> extensions = this.extensionManager.findExtensions(IJsonEventListenerExtension.class);

		if (extensions == null || extensions.isEmpty() || _object == null) {
			return;
		}

		String jsonValue = "";
		try {
			jsonValue = this.objectMapper.writeValueAsString(_object);
		} catch (final JsonProcessingException e) {
			LOGGER.error("Event has been rejected since it could not be converted into Json {}={}", _object, _object.getClass().getName(), e);

		}

		for (final IJsonEventListenerExtension extension : extensions) {
			extension.notifyEvent(new JsonEvent(_object.getClass().getName(), jsonValue));
		}

	}
}
