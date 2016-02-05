package fr.echoes.lab.ksf.extensions.events;

/**
 * This class describes an event once converted into Json.
 *
 * @author sleroy
 *
 */
public class JsonEvent {
	private final String eventName; // Nom qualifié de l'évènement
	
	private final String jsonData;
	
	public JsonEvent(final String _eventName, final String _json) {
		jsonData = _json;
		eventName = _eventName;
	}
	
	public String getEventName() {
		return eventName;
	}
	
	public String getJsonData() {
		return jsonData;
	}
	
}
