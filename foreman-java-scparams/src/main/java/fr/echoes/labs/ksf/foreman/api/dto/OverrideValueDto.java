package fr.echoes.labs.ksf.foreman.api.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterOverrideValue;

/**
 * DTO containing the fields required by the POST 
 * and PUT methods of the SmartClassParameterOverrideValue API
 * @author dmichel
 */
public class OverrideValueDto {

	private String match;
	private String value;
	private String usePuppetDefault;
	
	public OverrideValueDto() {
		// default constructor
	}
	
	public OverrideValueDto(final SmartClassParameterOverrideValue overrideValue) {
		this.match = overrideValue.getMatch();
		this.value = overrideValue.getValue();
		this.usePuppetDefault = Boolean.toString(overrideValue.getUsePuppetDefault());
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

	public void setValue(String value) {
		this.value = value;
	}

	@JsonGetter("use_puppet_default")
	public String getUsePuppetDefault() {
		return usePuppetDefault;
	}

	public void setUsePuppetDefault(String usePuppetDefault) {
		this.usePuppetDefault = usePuppetDefault;
	}	
	
}
