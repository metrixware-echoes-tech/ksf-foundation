package fr.echoes.labs.foremanapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VolumesAttributes {

	@JsonProperty("_delete")
	public String _delete = "";

	@JsonProperty("pool_name")
	public String pool_name = "default";

	@JsonProperty("capacity")
	public String capacity = "10G";

	@JsonProperty("allocation")
	public String allocation = "0G";

	@JsonProperty("format_type")
	public String format_type = "qcow2";

}
