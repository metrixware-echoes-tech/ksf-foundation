package fr.echoes.labs.foremanclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComputeAttributesBridge extends ComputeAttributes {

	@JsonProperty("type")
	public String type = "bridge";

	@JsonProperty("bridge")
	public String bridge = "br0";

	@JsonProperty("model")
	public String model = "virtio";
}
