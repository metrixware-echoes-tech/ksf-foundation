package fr.echoes.labs.ksf.cc.extmanager.events;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.Subscribe;
import com.tocea.corolla.cqrs.annotations.EventHandler;

import fr.echoes.labs.ksf.cc.api.extensions.events.IEventListenerExtension;
import fr.echoes.labs.pluginfwk.api.extension.ExtensionManager;

@EventHandler
public class EventListenerExtensionManager {

	private final ExtensionManager extensionManager;

	@Autowired
	public EventListenerExtensionManager(final ExtensionManager extensionManager) {
		super();
		this.extensionManager = extensionManager;
	}

	/**
	 * Listen to any event and dispatch them to the interested springplugins.
	 *
	 * @param _object
	 *            the event object
	 */
	@Subscribe
	public void listenAnyEvent(final Object _object) {

		final List<IEventListenerExtension> extensions = this.extensionManager.findExtensions(IEventListenerExtension.class);

		if (extensions == null || extensions.isEmpty()) {
			return;
		}
		for (final IEventListenerExtension extension : extensions) {
			extension.notifyEvent(_object);
		}

	}
}
