package fr.echoes.lab.foremanapi.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class NewHost {

	@JsonProperty("name")
	public String name;

	@JsonProperty("environment_id")
	public Integer environment_id;

	@JsonProperty("compute_profile_id")
	public String compute_profile_id;

	@JsonProperty("compute_resource_id")
	public String compute_resource_id;

	@JsonProperty("hostgroup_id")
	public Integer hostgroup_id;

	@JsonProperty("operatingsystem_id")
	public Integer operatingsystem_id;

	@JsonProperty("architecture_id")
	public Integer architecture_id;
}
