package com.tocea.corolla.products.events;

import com.tocea.corolla.products.domain.Project;

public class EventProjectUpdated {

	private Project project;

	public EventProjectUpdated() {
	}
	
	public EventProjectUpdated(final Project _project) {
		project = _project;
	}
	
	public Project getProject() {
		return project;
	}
	
	public void setProject(final Project _project) {
		project = _project;
	}
	
	@Override
	public String toString() {
		return "EventProjectUpdated [project=" + project + "]";
	}

}
