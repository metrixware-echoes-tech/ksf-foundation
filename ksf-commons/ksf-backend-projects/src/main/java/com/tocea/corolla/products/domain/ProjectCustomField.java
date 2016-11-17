package com.tocea.corolla.products.domain;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

public class ProjectCustomField {

	private String title;
	private String name;
	private Class<?> type;
	private List<Object> listValues = Lists.newArrayList();
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Class<?> getType() {
		return type;
	}
	
	public void setType(Class<?> type) {
		this.type = type;
	}
	
	public List<Object> getListValues() {
		return listValues;
	}
	
	public void setListValues(final Collection<Object> listValues) {
		this.listValues.addAll(listValues);
	}
	
	public void addListValue(final Object listValue) {
		this.listValues.add(listValue);
	}
	
	public String toString() {
		return new StringBuilder(this.name)
			.append("[title=").append(this.title)
			.append(", type=").append(this.type.getName())
			.append(", listValues=").append(this.listValues.toString())
			.append(']')
			.toString();
	}
	
}
