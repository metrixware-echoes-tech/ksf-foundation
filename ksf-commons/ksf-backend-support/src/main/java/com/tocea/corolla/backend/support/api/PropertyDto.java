package com.tocea.corolla.backend.support.api;

import java.io.Serializable;

public class PropertyDto implements Serializable, Comparable<PropertyDto> {

    private String key;
    private String value;

    public PropertyDto() {
    }

    public PropertyDto(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return key + " = " + value;
    }

    @Override
    public int compareTo(PropertyDto o) {
        return getKey().toLowerCase().compareTo(o.getKey().toLowerCase());
    }

}
