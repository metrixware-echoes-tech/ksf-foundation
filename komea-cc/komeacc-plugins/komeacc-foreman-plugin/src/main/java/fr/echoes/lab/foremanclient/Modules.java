package fr.echoes.lab.foremanclient;

import java.util.HashMap;
import java.util.Map;

public class Modules {

	public final Map<String, Module> modules = new HashMap<String, Module>();


	public Module getOrCreateModule(String moduleName) {
		Module module = this.modules.get(moduleName);
		if (module == null) {
			module = new Module();
			this.modules.put(moduleName, module);
		}
		return module;
	}

}
