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

	@JsonProperty("hostgroup_id")
	public String hostgroup_id;

	@JsonProperty("operatingsystem_id")
	public String operatingsystem_id;

	@JsonProperty("architecture_id")
	public String architecture_id;

	@JsonProperty("domain_id")
	public String domain_id;

	@JsonProperty("root_pass")
	public String root_pass;

}
