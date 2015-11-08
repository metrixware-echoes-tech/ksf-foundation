package com.tocea.corolla.timetracker.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Activity {
	@Id
	@Field("_id")
	private String id;

	@Indexed
	private String name;
	
	public Activity() {
	}
        
        public Activity(String _name) {
            name = _name;
        }
	
	public Activity(final String _id, final String _activityName) {
		super();
		id = _id;
		name = _activityName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getId() {
		return id;
	}
	
	public void setName(final String _activityName) {
		name = _activityName;
	}
	
	public void setId(final String _id) {
		id = _id;
	}
	
	@Override
	public String toString() {
		return "Activity [id=" + id + ", activityName=" + name + "]";
	}
	
}