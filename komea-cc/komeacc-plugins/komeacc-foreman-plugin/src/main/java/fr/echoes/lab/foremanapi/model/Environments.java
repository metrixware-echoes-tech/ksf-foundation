package fr.echoes.lab.foremanapi.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Environments {
    @JsonProperty("results") public List<Environment> results = null;
}
