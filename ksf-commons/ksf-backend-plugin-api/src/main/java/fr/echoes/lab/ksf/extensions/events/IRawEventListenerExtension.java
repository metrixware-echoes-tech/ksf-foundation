package fr.echoes.lab.ksf.extensions.events;

/**
 * This extension allows a plugin to listen the event bus from KSF.
 *
 * @author sleroy
 *
 */
public interface IRawEventListenerExtension {
	
	void notifyEvent(Object _event);
}
