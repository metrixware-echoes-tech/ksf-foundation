package fr.echoes.lab.foremanapi.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PuppetClassParameters {

    @JsonProperty("results") public List<PuppetClassParameter> results = null;
}
