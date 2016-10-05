package fr.echoes.labs.ksf.foreman.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSetter;

public class PuppetClass {

	private Integer id;
	private String name;
	private String moduleName;
	
	private List<ForemanHostGroup> hostGroups;
	
	public PuppetClass() {
		// default constructor
	}
	
	public PuppetClass(final Integer id, final String name, final String moduleName) {
		this.id = id;
		this.name = name;
		this.moduleName = moduleName;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getModuleName() {
		return moduleName;
	}
	
	@JsonSetter("module_name")
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public List<ForemanHostGroup> getHostGroups() {
		return hostGroups;
	}
	
	@JsonSetter("hostgroups")
	public void setHostGroups(List<ForemanHostGroup> hostGroups) {
		this.hostGroups = hostGroups;
	}

	@Override
	public String toString() {
		return new StringBuilder("{ ")
			.append("id=").append(this.id)
			.append(", name=").append(this.name)
			.append(", moduleName=").append(this.moduleName)
			.append(" }")
			.toString();
	}
	
}
