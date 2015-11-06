package com.tocea.corolla.products.events;

import com.tocea.corolla.products.domain.ProjectBranch;

public class EventNewBranchCreated {
	
	private final ProjectBranch	createdBranch;
	private final ProjectBranch	originBranch;
	
	/**
	 *
	 * @param _newBranch
	 *            the created branch
	 * @param _originBranch
	 *            the possibly null branch served to clone
	 */
	public EventNewBranchCreated(final ProjectBranch _newBranch, final ProjectBranch _originBranch) {
		createdBranch = _newBranch;
		originBranch = _originBranch;
	}

	public ProjectBranch getCreatedBranch() {
		return createdBranch;
	}

	public ProjectBranch getOriginBranch() {
		return originBranch;
	}
	
	@Override
	public String toString() {
		return "EventNewBranchCreated [createdBranch=" + createdBranch + "]";
	}
	
}
