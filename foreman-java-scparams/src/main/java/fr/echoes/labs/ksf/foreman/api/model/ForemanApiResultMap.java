package fr.echoes.labs.ksf.foreman.api.model;

import java.util.List;
import java.util.Map;

public class ForemanApiResultMap {

	private Map<String, List<Object>> results;

	public Map<String, List<Object>> getResults() {
		return results;
	}

	public void setResults(Map<String, List<Object>> results) {
		this.results = results;
	}
	
}
