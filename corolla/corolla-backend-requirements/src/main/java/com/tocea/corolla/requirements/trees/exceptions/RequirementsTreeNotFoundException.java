/*
 * Corolla - A Tool to manage software requirements and test cases 
 * Copyright (C) 2015 Tocea
 * 
 * This file is part of Corolla.
 * 
 * Corolla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, 
 * or any later version.
 * 
 * Corolla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Corolla.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tocea.corolla.requirements.trees.exceptions;

import com.tocea.corolla.products.domain.ProjectBranch;
import com.tocea.corolla.utils.domain.KsfDomainException;

public class RequirementsTreeNotFoundException extends KsfDomainException {

	private static String MESSAGE = "Requirements Tree not found";
	
	public RequirementsTreeNotFoundException() {
		super(MESSAGE);
	}
	
	public RequirementsTreeNotFoundException(String msg) {
		super(MESSAGE+": "+msg);
	}
	
	public RequirementsTreeNotFoundException(ProjectBranch branch) {
		super(MESSAGE+" for branch "+branch.getName());
	}
}
