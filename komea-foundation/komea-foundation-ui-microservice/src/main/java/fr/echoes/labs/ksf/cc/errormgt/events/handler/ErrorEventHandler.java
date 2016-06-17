package fr.echoes.labs.ksf.cc.errormgt.events.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.tocea.corolla.cqrs.annotations.EventHandler;

import fr.echoes.labs.ksf.cc.errormgt.events.ErrorEvent;

@EventHandler
public class ErrorEventHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorEventHandler.class);

	/**
	 * Listen to any event and dispatch them to the interested plugins.
	 *
	 * @param _object
	 *            the event object
	 */
	@Subscribe
	public void listenAnyEvent(final ErrorEvent _object) {
		LOGGER.warn("[ERROR] Exception happened inside KSF with :");
		LOGGER.warn("[ERROR] Id {}", _object.getErrorID());
		LOGGER.warn("[ERROR] Message {}", _object.getMessage());
		LOGGER.warn("[ERROR] Trace {}", _object.getTrace());

	}

}
