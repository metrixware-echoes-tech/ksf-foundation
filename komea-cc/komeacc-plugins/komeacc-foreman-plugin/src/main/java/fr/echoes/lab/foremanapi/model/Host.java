package fr.echoes.lab.foremanapi.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Host {

	@JsonProperty("name") public String name;
	@JsonProperty("id") public String id;
	@JsonProperty("ip") public String ip;
	@JsonProperty("environment_id") public String environmentId;
	@JsonProperty("environment_name") public String environmentName;
	@JsonProperty("mac") public String mac;
	@JsonProperty("domain_id") public String domainId;
	@JsonProperty("domain_name") public String domainName;
	@JsonProperty("architecture_id") public String architectureId;
	@JsonProperty("architecture_name") public String architectureName;
	@JsonProperty("operatingsystem_id") public String operatingSystemId;
	@JsonProperty("operatingsystem_name") public String operatingSystemName;
	@JsonProperty("hostgroup_id") public String hostGroupId;
	@JsonProperty("hostgroup_name") public String hostGroupName;

}
