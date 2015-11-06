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
package com.tocea.corolla.users.rest;

/**
 * @author sleroy
 *
 */
public class JsonError {
	private String	errorMessage;

	public JsonError() {
		super();
	}

	/**
	 * @param _string
	 */
	public JsonError(final String _string) {
		this.errorMessage = _string;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}

	/**
	 * @param _errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(final String _errorMessage) {
		this.errorMessage = _errorMessage;
	}
}
