package com.tocea.corolla.products.events;

import com.tocea.corolla.products.domain.Project;

public class EventNewProjectCreated {
	
	private final Project createdProject;
	private final Integer	parentNodeID;

	public EventNewProjectCreated(final Project _project, final Integer _parentNodeID) {
		createdProject = _project;
		parentNodeID = _parentNodeID;
		
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final EventNewProjectCreated other = (EventNewProjectCreated) obj;
		if (createdProject == null) {
			if (other.createdProject != null) {
				return false;
			}
		} else if (!createdProject.equals(other.createdProject)) {
			return false;
		}
		if (parentNodeID == null) {
			if (other.parentNodeID != null) {
				return false;
			}
		} else if (!parentNodeID.equals(other.parentNodeID)) {
			return false;
		}
		return true;
	}
	
	public Project getCreatedProject() {
		return createdProject;
	}
	
	public Integer getParentNodeID() {
		return parentNodeID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (createdProject == null ? 0 : createdProject.hashCode());
		result = prime * result + (parentNodeID == null ? 0 : parentNodeID.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return "EventNewProjectCreated [createdProject=" + createdProject + ", parentNodeID=" + parentNodeID + "]";
	}
	
}
