package fr.echoes.labs.ksf.extensions.events;

import fr.echoes.labs.ksf.extensions.api.IExtension;

/**
 * This extension allows a plugin to listen the event bus from KSF.
 *
 * @author sleroy
 *
 */
public interface IEventListenerExtension extends IExtension{

	void notifyEvent(Object _event);
}
