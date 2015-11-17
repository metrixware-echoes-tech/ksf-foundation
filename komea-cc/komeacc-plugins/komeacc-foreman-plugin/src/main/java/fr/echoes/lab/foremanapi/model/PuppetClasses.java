package fr.echoes.lab.foremanapi.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PuppetClasses {

	@JsonProperty("total") public String total;
	//@JsonProperty("results") public List<PuppetClass> results = null;
}
