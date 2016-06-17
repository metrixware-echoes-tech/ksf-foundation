package com.tocea.corolla.products.events;

import com.tocea.corolla.products.domain.Project;

/**
 * @author dcollard
 *
 */
public class EventReleaseFinished {

	private final Project project;
	private final String releaseVersion;

	public EventReleaseFinished(final Project project, String releaseVersion) {
		this.project = project;
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
		return EventReleaseFinished.class.getName() + " [project=" + this.project + ", releaseVersion=" + this.releaseVersion + "]";
	}
}
