package com.tocea.corolla.products.events;

import com.tocea.corolla.products.domain.Project;

public class EventProjectDeleted {
	
	private final Project project;

	public EventProjectDeleted(final Project _project) {
		project = _project;
	}
	
	public Project getProject() {
		return project;
	}
	
	@Override
	public String toString() {
		return "EventProjectDeleted [project=" + project + "]";
	}
	
}
