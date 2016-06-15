package com.tocea.corolla.products.events;

import com.tocea.corolla.products.domain.Project;

/**
 * @author dcollard
 *
 */
public class EventReleaseCreated {

	private final Project project;
	private final String releaseVersion;
	private String username;

	public EventReleaseCreated(final Project project, String username, String releaseVersion) {
		this.project = project;
		this.username = username;
		this.releaseVersion = releaseVersion;
	}

	public Project getProject() {
		return this.project;
	}

	public String getReleaseVersion() {
		return this.releaseVersion;
	}

	@Override
	public String toString() {
		return EventReleaseCreated.class.getName() + " [project=" + this.project + ", username=" + this.username + ", releaseVersion=" + this.releaseVersion + "]";
	}

	public String getUsername() {
		return username;
	}

}
