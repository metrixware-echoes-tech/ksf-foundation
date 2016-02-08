package fr.echoes.labs.foremanclient;

public class Parameter {

	private String id;
	private Object value;
	private boolean override;

	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Object getValue() {
		return this.value;
	}
	public void setValue(Object values) {
		this.value = values;
	}
	public Boolean isOverride() {
		return this.override;
	}
	public void setOverride(boolean override) {
		this.override = override;
	}
}
