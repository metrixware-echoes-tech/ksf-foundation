package fr.echoes.labs.foremanapi.model;

import org.codehaus.jackson.annotate.JsonProperty;



public class HostGroupWrapper {

	@JsonProperty("hostgroup")
	public HostGroup hostGroup;

	public HostGroup getHostGroup() {
		return this.hostGroup;
	}

	public void setHostGroup(HostGroup hostGroup) {
		this.hostGroup = hostGroup;
	}


}
