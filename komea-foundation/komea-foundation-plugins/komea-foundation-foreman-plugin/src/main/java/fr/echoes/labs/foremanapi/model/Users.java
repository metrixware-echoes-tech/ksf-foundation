package fr.echoes.labs.foremanapi.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Users {

    @JsonProperty("results")
    public List<User> results = null;
}
