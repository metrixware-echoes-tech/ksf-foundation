package com.tocea.corolla.cqrs.gate;

public interface IEventBusService {

	/**
	 * Dispatches an event into the bus.
	 * 
	 * @param _event
	 *            the event to dispatch.
	 */
	void dispatchEvent(Object _event);

}
