package fr.echoes.lab.foremanapi.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class RoleWrapper {
	@JsonProperty("role")

	public NewRole role;

	public NewRole getRole() {
		return this.role;
	}

	public void setRole(NewRole role) {
		this.role = role;
	}
}
