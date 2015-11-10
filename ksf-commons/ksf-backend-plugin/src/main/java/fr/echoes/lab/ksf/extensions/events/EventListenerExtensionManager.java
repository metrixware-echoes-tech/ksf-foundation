package fr.echoes.lab.ksf.extensions.events;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.Subscribe;
import com.tocea.corolla.cqrs.annotations.EventHandler;

@EventHandler
public class EventListenerExtensionManager {
	@Autowired
	private IEventListenerExtension[] extensions;

	/**
	 * Listen to any event and dispatch them to the interested plugins.
	 *
	 * @param _object
	 *            the event object
	 */
	@Subscribe
	public void listenAnyEvent(final Object _object) {
		if (extensions == null || extensions.length == 0) {
			return;
		}
		for (final IEventListenerExtension extension : extensions) {
			extension.notifyEvent(_object);
		}

	}
}
