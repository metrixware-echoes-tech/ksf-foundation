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
package com.tocea.corolla.revisions.domain;

import org.javers.core.diff.changetype.ValueChange;

public class Change implements IChange {

	private String propertyName;
	
	private Class<?> propertyType;
	
	private Object leftValue;
	
	private Object rightValue;
	
	public Change() {
		
	}
	
	public Change(ValueChange valueChange) {
		
		this.propertyName = valueChange.getPropertyName();
		this.propertyType = valueChange.getProperty().getType();
		this.leftValue = valueChange.getLeft();
		this.rightValue = valueChange.getRight();
	}
	
	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Class<?> getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(Class<?> propertyType) {
		this.propertyType = propertyType;
	}

	public Object getLeftValue() {
		return leftValue;
	}

	public void setLeftValue(Object leftValue) {
		this.leftValue = leftValue;
	}

	public Object getRightValue() {
		return rightValue;
	}

	public void setRightValue(Object rightValue) {
		this.rightValue = rightValue;
	}
	
}