package fr.echoes.labs.foremanclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComputeAttributes {

	@JsonProperty("type")
	public String type = "network";

	@JsonProperty("network")
	public String network = "default";

	@JsonProperty("model")
	public String model = "virtio";
}
