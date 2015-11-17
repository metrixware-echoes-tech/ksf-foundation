package fr.echoes.lab.foremanclient;

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
	public boolean isOverride() {
		return this.override;
	}
	public void setOverride(boolean override) {
		this.override = override;
	}
}
