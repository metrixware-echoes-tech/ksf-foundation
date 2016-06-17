package fr.echoes.labs.foremanapi.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Environment {

	@JsonProperty("name") public String name;
	@JsonProperty("id") public String id;
	@JsonProperty("puppetclasses") public List<PuppetClass> puppetClasses = null;

}
