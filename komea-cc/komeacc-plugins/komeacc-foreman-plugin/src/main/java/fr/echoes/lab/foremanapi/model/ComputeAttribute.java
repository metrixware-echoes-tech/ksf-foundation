package fr.echoes.lab.foremanapi.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComputeAttribute {

//	@JsonProperty("cpus")
//	public String cpus = "1";
//
//	@JsonProperty("memory")
//	public String memory = "536870912";

	@JsonProperty("start")
	public String start = "1";

	@JsonProperty("volumes_attributes")
	public Map<String, VolumesAttributes> volumes_attributes = new HashMap<String, VolumesAttributes>();


	public ComputeAttribute() {
		this.volumes_attributes.put("0", new VolumesAttributes());
	}

}
