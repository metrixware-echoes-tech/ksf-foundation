package fr.echoes.labs.ksf.foreman.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fr.echoes.labs.ksf.foreman.api.utils.KeepAsJsonDeserializer;

public class SmartClassParameter {

	private Integer id;
	private String parameter;
	private String defaultValue;
	private PuppetClass puppetClass;
	private boolean usePuppetDefault;
	private boolean override;
	private List<SmartClassParameterOverrideValue> overrideValues;
	
	public SmartClassParameter() {
		// default constructor
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getParameter() {
		return parameter;
	}
	
	public void setParameter(final String parameter) {
		this.parameter = parameter;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
	
	@JsonSetter("default_value")
	@JsonDeserialize(using = KeepAsJsonDeserializer.class)
	public void setDefaultValue(final String defaultValue) {
		
		this.defaultValue = defaultValue;
	}	
	
	public List<SmartClassParameterOverrideValue> getOverrideValues() {
		return overrideValues;
	}
	
	@JsonSetter("override_values")
	public void setOverrideValues(List<SmartClassParameterOverrideValue> overrideValues) {
		this.overrideValues = overrideValues;
	}
	
	public PuppetClass getPuppetClass() {
		return puppetClass;
	}

	@JsonSetter("puppetclass")
	public void setPuppetClass(PuppetClass puppetClass) {
		this.puppetClass = puppetClass;
	}

	public boolean isUsePuppetDefault() {
		return usePuppetDefault;
	}
	
	@JsonSetter("use_puppet_default")
	public void setUsePuppetDefault(boolean usePuppetDefault) {
		this.usePuppetDefault = usePuppetDefault;
	}
	
	public boolean isOverride() {
		return override;
	}

	public void setOverride(boolean override) {
		this.override = override;
	}

	@Override
	public String toString() {
		return new StringBuilder("{ ")
			.append("puppetClass=").append(this.puppetClass != null ? this.puppetClass.toString() : null)
			.append(", parameter=").append(this.parameter)
			.append(", override=").append(this.override)
			.append(", defaultValue=").append(this.defaultValue)
			.append(", usePuppetDefault=").append(Boolean.toString(this.usePuppetDefault))
			.append(", overiddeValues=").append(this.overrideValues != null ? this.overrideValues.size() : 0)
			.append(" }")
			.toString();
	}	
	
}
