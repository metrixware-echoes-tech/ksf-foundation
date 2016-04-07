package com.tocea.corolla.products.events;

import com.tocea.corolla.products.domain.Project;

/**
 * @author dcollard
 *
 */
public class EventReleaseCreated {

	private final Project project;

	public EventReleaseCreated(final Project project) {
		this.project = project;
	}

	public Project getProject() {
		return this.project;
	}

	@Override
	public String toString() {
		return EventReleaseCreated.class.getName() + " [project=" + this.project + "]";
	}

}
