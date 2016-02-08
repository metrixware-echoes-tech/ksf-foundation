package fr.echoes.labs.foremanapi.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class SmartClassParameterWrapper {
	@JsonProperty("match") public String match;
	@JsonProperty("value") public String value;
}
