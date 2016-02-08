package fr.echoes.labs.foremanapi.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class UserWrapper {

	@JsonProperty("user")
	public NewUser user;

	public NewUser getUser() {
		return this.user;
	}

	public void setUser(NewUser user) {
		this.user = user;
	}
}
