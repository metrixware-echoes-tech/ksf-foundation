package fr.echoes.labs.foremanapi.model;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PuppetClasses {

	@JsonProperty("total") public String total;
	@JsonProperty("results") public Map<String, PuppetClass> results = new HashMap<String, PuppetClass>();
}
