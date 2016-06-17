package fr.echoes.labs.foremanclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComputeAttributesBridge {

	@JsonProperty("type")
	public String type = "bridge";

	@JsonProperty("network")
	public String network = "br0";

	@JsonProperty("model")
	public String model = "virtio";
}
