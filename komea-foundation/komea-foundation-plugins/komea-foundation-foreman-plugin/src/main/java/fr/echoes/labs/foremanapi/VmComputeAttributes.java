package fr.echoes.labs.foremanapi;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;



@JsonIgnoreProperties(ignoreUnknown = true)
public class VmComputeAttributes {

	@JsonProperty("state") public String state;

}
