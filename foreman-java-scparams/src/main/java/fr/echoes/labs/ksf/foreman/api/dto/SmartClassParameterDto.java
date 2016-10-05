package fr.echoes.labs.ksf.foreman.api.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameter;

/**
 * DTO containing fields required when calling the
 * POST and PUT method of the SmartClassParameter API
 * @author dmichel
 */
public class SmartClassParameterDto {

	private String override;
	private String defaultValue;
	private String usePuppetDefault;
	
	public SmartClassParameterDto() {
		// default constructor
	}
	
	public SmartClassParameterDto(final SmartClassParameter param) {
		this.override = Boolean.toString(param.isOverride());
		this.defaultValue = param.getDefaultValue();
		this.usePuppetDefault = Boolean.toString(param.isUsePuppetDefault());
	}

	public String getOverride() {
		return override;
	}
	
	public void setOverride(String override) {
		this.override = override;
	}
	
	@JsonGetter("default_value")
	public String getDefaultValue() {
		return defaultValue;
	}
	
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	@JsonGetter("use_puppet_default")
	public String getUsePuppetDefault() {
		return usePuppetDefault;
	}

	public void setUsePuppetDefault(String usePuppetDefault) {
		this.usePuppetDefault = usePuppetDefault;
	}
	
}
