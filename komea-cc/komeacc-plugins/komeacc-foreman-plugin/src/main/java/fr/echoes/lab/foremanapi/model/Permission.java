package fr.echoes.lab.foremanapi.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Permission {

	@JsonProperty("name") public String name;
	@JsonProperty("id") public String id;
	@JsonProperty("resource_type") public String resource_type;

}
