package fr.echoes.lab.foremanapi.model;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public class OverrideValueWrapper {
	@JsonProperty("override_value") public Map<String, Object> override_value;
}
