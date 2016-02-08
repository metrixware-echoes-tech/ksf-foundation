package fr.echoes.labs.foremanapi.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {
	
	@JsonProperty("builtin") public String builtin;
	@JsonProperty("name") public String name;
	@JsonProperty("id") public String id;
	
	@JsonProperty("filters") public List<Filter> filters = null;
	
}
