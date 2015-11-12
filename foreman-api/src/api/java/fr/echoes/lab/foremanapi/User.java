package fr.echoes.lab.foremanapi;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import fr.echoes.lab.foremanapi.model.Role;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	@JsonProperty("id") public String id;
	@JsonProperty("login") public String login;
	@JsonProperty("roles") public List<Role> roles = null;
}
