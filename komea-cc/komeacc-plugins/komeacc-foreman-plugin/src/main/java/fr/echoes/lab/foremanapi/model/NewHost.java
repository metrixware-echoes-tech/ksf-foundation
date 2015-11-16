package fr.echoes.lab.foremanapi.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class NewHost {

	@JsonProperty("name")
	public String name;

	@JsonProperty("environment_id")
	public String environment_id;

	@JsonProperty("compute_profile_id")
	public String compute_profile_id;

	@JsonProperty("compute_resource_id")
	public String compute_resource_id;

}
