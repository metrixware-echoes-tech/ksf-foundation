package com.tocea.corolla.cqrs.gate;

/**
 * The Interface IEventBus defines the methods offered by the event bus
 */
public interface IEventBus {

	/**
	 * Dispatches an event into the bus.
	 *
	 * @param _event
	 *            the event to dispatch.
	 */
	void dispatchEvent(Object _event);

}
