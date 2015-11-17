package fr.echoes.lab.foremanapi.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PuppetClassParameter {

	@JsonProperty("id") public String id;
	@JsonProperty("parameter") public String parameter;
	@JsonProperty("default_value") public Object default_value;
	@JsonProperty("override") public Boolean override;
}
