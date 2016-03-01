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
package com.tocea.corolla.products.domain;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.common.collect.Lists;
import com.tocea.corolla.utils.domain.ObjectValidation;

@Document
public class Project implements Serializable {

	private static final long serialVersionUID = -6535458310998167446L;

	@Id
    @Field("_id")
    private String id;

    @NotEmpty
    @Size(min = 3, max = 50)
    @Pattern(regexp = ObjectValidation.URL_SAFE_PATTERN)
    private String key;

    @NotEmpty
    @Size(min = 3, max = 100)
    private String name;

    private String statusId;

    private String categoryId;

    private String ownerId;

    private String description;

    private URL image;

    private List<String> tags = Lists.newArrayList();

    private String parentId;

    public String getCategoryId() {
        return this.categoryId;
    }

    public String getDescription() {
        return this.description;
    }

    public String getId() {
        return this.id;
    }

    public URL getImage() {
        return this.image;
    }

    public String getKey() {
        return this.key;
    }

    public String getName() {
        return this.name;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public String getStatusId() {
        return this.statusId;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public String getParentId() {
    	return this.parentId;
    }

    public void setCategoryId(final String categoryId) {
        this.categoryId = categoryId;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setImage(final URL image) {
        this.image = image;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setOwnerId(final String ownerId) {
        this.ownerId = ownerId;
    }

    public void setStatusId(final String statusId) {
        this.statusId = statusId;
    }

    public void setTags(final List<String> tags) {
        this.tags = tags;
    }

    public void setParentId(final String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
    	return "Project{" + "id=" + this.id +
    			", key=" + this.key +
    			", name=" + this.name +
    			", statusId=" + this.statusId +
    			", categoryId=" + this.categoryId +
    			", ownerId=" + this.ownerId +
    			", description=" + this.description +
    			", image=" + this.image +
    			", tags=" + this.tags +
    			", parentId=" + this.parentId + '}';
    }
}
