package fr.echoes.labs.ksf.foreman.api.model;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonSetter;

public class ForemanHostGroup {

	private Integer id;
	private String name;
	private Integer parentId;
	private String parentName;
	
	public ForemanHostGroup() {
		// default constructor
	}
	
	public ForemanHostGroup(final Integer id, final String name) {
		this.id = id;
		this.name = name;
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
	
	public Integer getParentId() {
		return parentId;
	}
	
	@JsonSetter("parent_id")
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	public String getParentName() {
		return parentName;
	}
	
	@JsonSetter("parent_name")
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	public String getFullName() {
		
		if (StringUtils.isEmpty(this.parentName)) {
			return this.name;
		}
		
		return this.parentName + '/' + this.name;
	}

	@Override
	public String toString() {
		return "{ id="+id+", name="+name+", parent_id="+parentId+", parent_name="+parentName+" }";
	}
}
