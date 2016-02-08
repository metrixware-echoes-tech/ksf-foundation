package fr.echoes.labs.foremanclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NetworkInterface {
	@JsonProperty("_destroy")
	public String _destroy = "0";

	@JsonProperty("type")
	public String type = "Nic::Managed";

	@JsonProperty("mac")
	public String mac = "";

	@JsonProperty("identifier")
	public String identifier = "";

	@JsonProperty("name")
	public String name;

	@JsonProperty("domain_id")
	public String domain_id = "2";

	@JsonProperty("subnet_id")
	public String subnet_id = "1";

	@JsonProperty("ip")
	public String ip;

	@JsonProperty("managed")
	public String managed = "1";

	@JsonProperty("primary")
	public String primary = "1";

	@JsonProperty("provision")
	public String provision = "1";

	@JsonProperty("virtual")
	public String virtual = "0";

	@JsonProperty("tag")
	public String tag = "";

	@JsonProperty("attached_to")
	public String attached_to = "";

	@JsonProperty("compute_attributes")
	public ComputeAttributes compute_attributes = new ComputeAttributes();


}
