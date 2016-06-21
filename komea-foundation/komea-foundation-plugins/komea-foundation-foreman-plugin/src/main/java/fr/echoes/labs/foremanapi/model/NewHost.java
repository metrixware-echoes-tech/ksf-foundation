package fr.echoes.labs.foremanapi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.echoes.labs.foremanclient.model.NetworkInterface;


@JsonIgnoreProperties(ignoreUnknown = true)
public class NewHost {

	@JsonProperty("name")
	public String name;

	@JsonProperty("hostgroup_id")
	public String hostgroup_id;

	@JsonProperty("compute_resource_id")
	public String compute_resource_id;

	@JsonProperty("environment_id")
	public String environment_id;

	@JsonProperty("puppet_ca_proxy_id")
	public String puppet_ca_proxy_id = "2";

	@JsonProperty("puppet_proxy_id")
	public String puppet_proxy_id = "2";

	@JsonProperty("managed")
	public Boolean managed = true;

	@JsonProperty("type")
	public String type = "Host::Managed";

	@JsonProperty("puppetclass_ids")
	public List<String> puppetclass_ids = new ArrayList<String>();

	@JsonProperty("compute_profile_id")
	public String compute_profile_id;

	@JsonProperty("operatingsystem_id")
	public String operatingsystem_id;

	@JsonProperty("architecture_id")
	public String architecture_id;

	@JsonProperty("domain_id")
	public String domain_id;

	@JsonProperty("root_pass")
	public String root_pass;

	@JsonProperty("subnet_id")
	public String subnet_id;

	@JsonProperty("mac")
	public String mac;

	@JsonProperty("build")
	public String build = "1";

	@JsonProperty("provision_method")
	public String provision_method = "build";

	@JsonProperty("interfaces_attributes")
	public Map<String, NetworkInterface> interfaces_attributes = new HashMap<String, NetworkInterface>();

	@JsonProperty("compute_attributes")
	public ComputeAttribute compute_attributes = new ComputeAttribute();

	@JsonProperty("medium_id")
	public String medium_id;

	@JsonProperty("ptable_id")
	public String ptable_id;

}
