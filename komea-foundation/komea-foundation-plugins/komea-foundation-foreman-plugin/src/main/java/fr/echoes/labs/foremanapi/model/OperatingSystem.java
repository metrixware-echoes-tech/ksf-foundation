package fr.echoes.labs.foremanapi.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OperatingSystem {

	@JsonProperty("id")               public String id;
	@JsonProperty("name")             public String name;
	@JsonProperty("title")            public String title;
	@JsonProperty("description")      public String description;
	@JsonProperty("major")            public String major;
	@JsonProperty("minor")            public String minor;
	@JsonProperty("family")           public String family;

}
