package fr.echoes.labs.ksf.cc.api.extensions.events;

import fr.echoes.labs.pluginfwk.api.extension.IExtension;

/**
 * This extension allows a plugin to listen the event bus from KSF.
 *
 * @author sleroy
 *
 */
public interface IEventListenerExtension extends IExtension{

	void notifyEvent(Object _event);
}
