package fr.echoes.lab.ksf.extensions.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.Subscribe;
import com.tocea.corolla.cqrs.annotations.EventHandler;

/**
 * This extension manager dispatch events encapsulated into an handy wrapper.
 *
 * @author sleroy
 *
 */
@EventHandler
public class JsonEventListenerExtensionManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonEventListenerExtensionManager.class);

	private final IJsonEventListenerExtension[] extensions;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	public JsonEventListenerExtensionManager(final IJsonEventListenerExtension[] _extensions) {
		extensions = _extensions;
	}

	/**
	 * Listen to any event and dispatch them to the interested plugins.
	 *
	 * @param _object
	 *            the event object
	 */
	@Subscribe
	public void listenAnyEvent(final Object _object) {
		if (extensions == null || extensions.length == 0 || _object == null) {
			return;
		}

		String jsonValue = "";
		try {
			jsonValue = objectMapper.writeValueAsString(_object);
		} catch (final JsonProcessingException e) {
			LOGGER.error("Event has been rejected since it could not be converted into Json {}={}", _object,
					_object.getClass().getName(), e);

		}

		for (final IJsonEventListenerExtension extension : extensions) {
			extension.notifyEvent(new JsonEvent(_object.getClass().getName(), jsonValue));
		}

	}
}
