package fr.echoes.labs.foremanapi.model;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HostPuppetClasses {

	@JsonProperty("results") public Map<String, HostPuppetClass[]> results = null;
}
