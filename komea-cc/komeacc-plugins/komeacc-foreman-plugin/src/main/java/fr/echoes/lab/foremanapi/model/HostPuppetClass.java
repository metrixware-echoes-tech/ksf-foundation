package fr.echoes.lab.foremanapi.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class HostPuppetClass {
		@JsonProperty("id") public String id;
		@JsonProperty("name") public String name;
}
