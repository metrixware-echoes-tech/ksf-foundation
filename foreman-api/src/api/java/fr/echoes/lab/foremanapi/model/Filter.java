package fr.echoes.lab.foremanapi.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Filter {
	
	@JsonProperty("search")        public String search;
	@JsonProperty("resource_type") public String resource_type;
	@JsonProperty("unlimited?")    public Boolean unlimited;
	@JsonProperty("id")            public String id;
	@JsonProperty("role")          public Role role;
	@JsonProperty("permissions")   public List<Permission> permissions;

//	public Filter copy() {
//		Filter copy = new Filter();
//		copy.search = search;
//		copy.resource_type = resource_type;
//		copy.unlimited = unlimited;
//		copy.role = role;
//		copy.permissions = permissions;
//		return copy;
//	}

//	@JsonProperty("total") public Integer total;
//	@JsonProperty("subtotal") public Integer subtotal;
//	@JsonProperty("page") public Integer page;
//	@JsonProperty("per_page") public Integer per_page;
//	@JsonProperty("search") public String search;
//	@JsonProperty("sort") public Object sort;
//    @JsonProperty("results") public List<HG> results = null;
}
