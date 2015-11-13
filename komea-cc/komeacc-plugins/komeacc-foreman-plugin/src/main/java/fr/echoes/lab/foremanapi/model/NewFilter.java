package fr.echoes.lab.foremanapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewFilter {
	@JsonProperty("role_id")            public String role_id;
	@JsonProperty("search")             public String search;
	@JsonProperty("permission_ids")     public String[] permission_ids;
//	@JsonProperty("organisation_ids")   public String organisation_ids;
//	@JsonProperty("location_ids")       public String location_ids;
}
