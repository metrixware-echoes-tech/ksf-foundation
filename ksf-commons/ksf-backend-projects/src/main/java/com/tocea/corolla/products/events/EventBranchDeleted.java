package com.tocea.corolla.products.events;

import com.tocea.corolla.products.domain.ProjectBranch;

/**
 * This event is fired when a project branch is deleted.
 *
 * @author sleroy
 *
 */
public class EventBranchDeleted {

	private final ProjectBranch branch;
	
	public EventBranchDeleted(final ProjectBranch _branch) {
		branch = _branch;
	}

	public ProjectBranch getBranch() {
		return branch;
	}
	
	@Override
	public String toString() {
		return "EventBranchDeleted [branch=" + branch + "]";
	}
	
}
