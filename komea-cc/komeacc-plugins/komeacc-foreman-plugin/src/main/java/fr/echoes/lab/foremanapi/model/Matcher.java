package fr.echoes.lab.foremanapi.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Matcher {
	@JsonProperty("id") public String id;
	@JsonProperty("match") public String match;
	@JsonProperty("value") public String value;
}
