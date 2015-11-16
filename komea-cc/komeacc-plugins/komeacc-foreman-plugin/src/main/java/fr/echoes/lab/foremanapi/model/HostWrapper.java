package fr.echoes.lab.foremanapi.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class HostWrapper {

	@JsonProperty("host")
	public NewHost host;

	public NewHost getHost() {
		return this.host;
	}

	public void setHost(NewHost host) {
		this.host = host;
	}

}
