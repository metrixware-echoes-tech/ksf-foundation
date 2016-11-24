package fr.echoes.labs.ksf.foreman.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fr.echoes.labs.ksf.foreman.api.utils.KeepAsJsonDeserializer;

public class SmartVariable {

	private Integer id;
	private String variable;
	private String type;
	private Integer puppetClassId;
	private String puppetClassName;
	private String defaultValue;
	
	private List<SmartClassParameterOverrideValue> overrideValues;
	
	public SmartVariable() {
		// default constructor
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getVariable() {
		return variable;
	}
	
	public void setVariable(String variable) {
		this.variable = variable;
	}
	
	public String getType() {
		return type;
	}
	
	@JsonSetter("parameter_type")
	public void setType(String type) {
		this.type = type;
	}
	
	public Integer getPuppetClassId() {
		return puppetClassId;
	}
	
	@JsonSetter("puppetclass_id")
	public void setPuppetClassId(Integer puppetClassId) {
		this.puppetClassId = puppetClassId;
	}
	
	public String getPuppetClassName() {
		return puppetClassName;
	}
	
	@JsonSetter("puppetclass_name")
	public void setPuppetClassName(String puppetClassName) {
		this.puppetClassName = puppetClassName;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
	
	@JsonSetter("default_value")
	@JsonDeserialize(using = KeepAsJsonDeserializer.class)
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public List<SmartClassParameterOverrideValue> getOverrideValues() {
		return overrideValues;
	}
	
	@JsonSetter("override_values")
	public void setOverrideValues(List<SmartClassParameterOverrideValue> overrideValues) {
		this.overrideValues = overrideValues;
	}
	
	public String toString() {
		return new StringBuilder("{ ")
			.append("id=").append(this.id)
			.append(", variable=").append(this.variable)
			.append(", puppetClassId=").append(this.puppetClassId)
			.append(", type=").append(this.type)
			.append(", defaultValue=").append(this.defaultValue)
			.append(" }")
			.toString();	
	}
	
}
