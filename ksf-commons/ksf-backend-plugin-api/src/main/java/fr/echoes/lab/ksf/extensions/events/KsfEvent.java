package fr.echoes.lab.ksf.extensions.events;

public class KsfEvent {
	String eventName; // Nom qualifié de l'évènement
	
	public String getEventName() {
		return eventName;
	}
	
	public void setEventName(String _eventName) {
		eventName = _eventName;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object _data) {
		data = _data;
	}
	
	Object data;
	
}
