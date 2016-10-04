package fr.echoes.labs.ksf.foreman.api.model;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.common.collect.Sets;

public class ForemanHost {

	private String id;
	private String name;
	private String hostGroupName;
	private List<ForemanHostParameter> parameters;
	private List<PuppetClass> puppetclasses;
	
	public ForemanHost() {
		// default constructor
	}
	
	public ForemanHost(final String id, final String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<ForemanHostParameter> getParameters() {
		return parameters;
	}
	
	public void setParameters(List<ForemanHostParameter> parameters) {
		this.parameters = parameters;
	}
	
	public List<PuppetClass> getPuppetclasses() {
		return puppetclasses;
	}
	
	@JsonSetter("all_puppetclasses")
	public void setPuppetclasses(List<PuppetClass> puppetclasses) {
		this.puppetclasses = puppetclasses;
	}

	public String getHostGroupName() {
		return hostGroupName;
	}
	
	@JsonSetter("hostgroup_name")
	public void setHostGroupName(String hostGroupName) {
		this.hostGroupName = hostGroupName;
	}
	
	public Set<String> getPuppetModules() {
		
		final Set<String> puppetModules = Sets.newHashSet();
		for (final PuppetClass puppetClass : this.getPuppetclasses()) {
			puppetModules.add(puppetClass.getModuleName());
		}
		
		return puppetModules;
	}
	
}
