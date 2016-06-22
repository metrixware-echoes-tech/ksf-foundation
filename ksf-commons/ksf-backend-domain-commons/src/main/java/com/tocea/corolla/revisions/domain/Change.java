/*
 * Corolla - A Tool to manage software requirements and test cases
 * Copyright (C) 2015 Tocea
 * This file is part of Corolla.
 * Corolla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License,
 * or any later version.
 * Corolla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Corolla. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tocea.corolla.revisions.domain;

import org.javers.core.diff.changetype.ValueChange;

public class Change implements IChange {

    private String propertyName;

    private Object leftValue;

    private Object rightValue;

    public Change() {

    }

    /**
     * Instantiates a new change.
     *
     * @param valueChange the value change
     */
    public Change(final ValueChange valueChange) {

        this.propertyName = valueChange.getPropertyName();
        this.leftValue = valueChange.getLeft();
        this.rightValue = valueChange.getRight();
    }

    @Override
    public Object getLeftValue() {
        return this.leftValue;
    }

    @Override
    public String getPropertyName() {
        return this.propertyName;
    }

    @Override
    public Object getRightValue() {
        return this.rightValue;
    }

    public void setLeftValue(final Object leftValue) {
        this.leftValue = leftValue;
    }

    public void setPropertyName(final String propertyName) {
        this.propertyName = propertyName;
    }

    public void setRightValue(final Object rightValue) {
        this.rightValue = rightValue;
    }

}
