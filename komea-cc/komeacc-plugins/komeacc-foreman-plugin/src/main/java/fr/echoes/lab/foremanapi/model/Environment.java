package fr.echoes.lab.foremanapi.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Environment {
	@JsonProperty("name") public String name;
	@JsonProperty("id") public Integer id;
}
