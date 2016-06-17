package fr.echoes.labs.foremanapi.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class FilterWrapper {

	@JsonProperty("filter")
	public NewFilter filter;

	public NewFilter getFilter() {
		return this.filter;
	}

	public void setFilter(NewFilter filter) {
		this.filter = filter;
	}
}
