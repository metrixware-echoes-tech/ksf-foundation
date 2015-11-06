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
package com.tocea.corolla.trees.dto;

import java.util.Collection;
import java.util.Map;

import com.tocea.corolla.trees.dto.JsTreeNodeDTO;

public class JsTreeNodeDTO {

	private String text = "";
	
	private String icon = "";
	
	private String type;
	
	private Collection<JsTreeNodeDTO> children;
	
	private Map<String, String> a_attr;
	
	public JsTreeNodeDTO() {
		
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Collection<JsTreeNodeDTO> getChildren() {
		return children;
	}

	public void setChildren(Collection<JsTreeNodeDTO> children) {
		this.children = children;
	}

	public Map<String, String> getA_attr() {
		return a_attr;
	}

	public void setA_attr(Map<String, String> a_attr) {
		this.a_attr = a_attr;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}