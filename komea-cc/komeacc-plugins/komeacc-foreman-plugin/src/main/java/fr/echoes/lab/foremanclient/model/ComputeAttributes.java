package fr.echoes.lab.foremanclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComputeAttributes {

	@JsonProperty("type")
	public String type = "network";

	@JsonProperty("network")
	public String network = "ksf_ads";

	@JsonProperty("model")
	public String model = "virtio";
}
