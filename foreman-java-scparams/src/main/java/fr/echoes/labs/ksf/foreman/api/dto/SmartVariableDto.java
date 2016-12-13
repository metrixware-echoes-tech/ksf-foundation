package fr.echoes.labs.ksf.foreman.api.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

import fr.echoes.labs.ksf.foreman.api.model.SmartVariable;

public class SmartVariableDto {

	private String variable;
	private Integer puppetClassId;
	private String defaultValue;
	private String type;
	
	public SmartVariableDto() {
		// default constructor
	}
	
	public SmartVariableDto(final SmartVariable var) {
		this.variable = var.getVariable();
		this.puppetClassId = var.getPuppetClassId();
		this.type = var.getType();
		this.defaultValue = var.getDefaultValue();
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}
	
	@JsonGetter("puppetclass_id")
	public Integer getPuppetClassId() {
		return puppetClassId;
	}
	
	public void setPuppetClassId(Integer puppetClassId) {
		this.puppetClassId = puppetClassId;
	}

	@JsonGetter("default_value")
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@JsonGetter("variable_type")
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}
