package fr.echoes.labs.foremanclient;

import java.util.HashMap;
import java.util.Map;


public class PuppetClass {

	public final Map<String, Parameter> parameters = new HashMap<String, Parameter>();

	public String id;

	public Parameter getOrCreateParameter(String moduleName) {
		Parameter parameter = this.parameters.get(moduleName);
		if (parameter == null) {
			parameter = new Parameter();
			this.parameters.put(moduleName, parameter);
		}
		return parameter;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}
}
