package fr.echoes.lab.ksf.extensions.events;

/**
 * This extension allows a plugin to listen the event bus from KSF.
 *
 * @author sleroy
 *
 */
public interface IEventListenerExtension {

	void notifyEvent(KsfEvent _event);
}
