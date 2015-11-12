package fr.echoes.lab.foremanapi.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Hostgroups {
	@JsonProperty("total") public Integer total;
	@JsonProperty("subtotal") public Integer subtotal;
	@JsonProperty("page") public Integer page;
	@JsonProperty("per_page") public Integer per_page;
	@JsonProperty("search") public String search;
	@JsonProperty("sort") public Object sort;
    @JsonProperty("results") public List<HostGroup> results = null;
}
