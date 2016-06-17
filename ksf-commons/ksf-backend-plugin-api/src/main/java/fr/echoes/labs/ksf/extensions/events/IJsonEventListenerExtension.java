package fr.echoes.labs.ksf.extensions.events;

import fr.echoes.labs.ksf.extensions.api.IExtension;

/**
 * This extension allows a plugin to listen the event bus from KSF.
 *
 * @author sleroy
 *
 */
public interface IJsonEventListenerExtension extends IExtension {
	
	void notifyEvent(JsonEvent _event);
}
