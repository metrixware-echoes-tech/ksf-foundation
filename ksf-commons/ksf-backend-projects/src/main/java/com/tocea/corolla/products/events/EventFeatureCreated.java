package com.tocea.corolla.products.events;

import com.tocea.corolla.products.domain.Project;

/**
 * @author dcollard
 *
 */
public class EventFeatureCreated {

	private final Project project;
	private final String featureId;
	private final String featureSubject;
	private String username;

	public EventFeatureCreated(Project project, String username, String featureId, String featureSubject) {
		this.project = project;
		this.username = username;
		this.featureId = featureId;
		this.featureSubject = featureSubject;
	}

	/**
	 * @return the project
	 */	
	public Project getProject() {
		return this.project;
	}

	/**
	 * @return the featureId
	 */
	public String getFeatureId() {
		return featureId;
	}

	/**
	 * @return the featureSubject
	 */
	public String getFeatureSubject() {
		return featureSubject;
	}

	@Override
	public String toString() {
		return EventFeatureCreated.class.getName() + " [project=" + this.project + ", username=" + this.username + ", featureId=" + this.featureId + ", featureSubject=" + this.featureSubject + "]";
	}

	public String getUsername() {
		return username;
	}
}
