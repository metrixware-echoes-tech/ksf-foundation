package com.tocea.corolla.products.events;

import com.tocea.corolla.products.domain.ProjectBranch;

public class EventProjectBranchUpdated {
	
	private final ProjectBranch branch;

	public EventProjectBranchUpdated(final ProjectBranch _branch) {
		branch = _branch;
	}
	
	public ProjectBranch getBranch() {
		return branch;
	}
	
	@Override
	public String toString() {
		return "EventProjectBranchUpdated [branch=" + branch + "]";
	}
	
}
