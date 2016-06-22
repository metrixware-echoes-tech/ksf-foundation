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
package com.tocea.corolla.products.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.tocea.corolla.products.dao.IProjectStatusDAO;
import com.tocea.corolla.products.domain.ProjectStatus;
import com.tocea.corolla.revisions.domain.IChange;
import com.tocea.corolla.revisions.services.IChangeFormatter;

@Component
public class ProjectChangeFormatter implements IChangeFormatter {

    @Autowired
    private IProjectStatusDAO statusDAO;

    @Override
    public String format(IChange change, String target) {

        Object value = target.equals("R") ? change.getRightValue() : change.getLeftValue();

        if (value != null && List.class.isAssignableFrom(value.getClass())) {
            return Joiner.on(',').join((Iterable<?>) value);
        }

        if (change.getPropertyName().equals("statusId")) {
            ProjectStatus status = statusDAO.findOne((String) value);
            return status.getName();
        }

        return value != null ? value.toString() : "";
    }

}
