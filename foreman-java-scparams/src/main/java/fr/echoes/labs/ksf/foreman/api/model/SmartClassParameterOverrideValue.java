package fr.echoes.labs.ksf.foreman.api.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fr.echoes.labs.ksf.foreman.api.utils.KeepAsJsonDeserializer;

public class SmartClassParameterOverrideValue {

	private Integer id;
	private String match;
	private String value;
	private Boolean usePuppetDefault;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getMatch() {
		return match;
	}
	
	public void setMatch(String match) {
		this.match = match;
	}
	
	public String getValue() {
		return value;
	}
	
	@JsonDeserialize(using=KeepAsJsonDeserializer.class)
	public void setValue(String value) {
		this.value = value;
	}
	
	public Boolean getUsePuppetDefault() {
		return usePuppetDefault;
	}
	
	@JsonSetter("use_puppet_default")
	public void setUsePuppetDefault(Boolean usePuppetDefault) {
		this.usePuppetDefault = usePuppetDefault;
	}
	
}
