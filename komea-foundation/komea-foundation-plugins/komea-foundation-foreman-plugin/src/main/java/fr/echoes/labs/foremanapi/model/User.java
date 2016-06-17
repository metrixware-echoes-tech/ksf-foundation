package fr.echoes.labs.foremanapi.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import fr.echoes.labs.foremanapi.model.Role;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	@JsonProperty("id") public String id;
	@JsonProperty("login") public String login;
	@JsonProperty("roles") public List<Role> roles = null;
}
