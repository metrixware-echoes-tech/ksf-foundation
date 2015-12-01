package fr.echoes.lab.foremanclient;

import java.util.HashMap;
import java.util.Map;


public class Module {

	public String id;
	public final Map<String, PuppetClass> puppetClasses = new HashMap<String, PuppetClass>();

	public PuppetClass getOrCreatePuppetClass(String puppetClassName) {
		PuppetClass puppetClass = this.puppetClasses.get(puppetClassName);
		if (puppetClass == null) {
			puppetClass = new PuppetClass();
			this.puppetClasses.put(puppetClassName, puppetClass);
		}
		return puppetClass;
	}
}
