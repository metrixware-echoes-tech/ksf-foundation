/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocea.corolla.timetracker.events;

import com.tocea.corolla.timetracker.domain.Activity;

/**
 *
 * @author sleroy
 */
public class EventActivityDeleted {
	private Activity activity;
	
	public EventActivityDeleted(final Activity _activity) {
		super();
		activity = _activity;
	}
	
	/**
	 * @return the activity
	 */
	public Activity getActivity() {
		return activity;
	}
	
	/**
	 * @param activity the activity to set
	 */
	public void setActivity(final Activity activity) {
		this.activity = activity;
	}
	
	@Override
	public String toString() {
		return "EventActivityDeleted [activity=" + activity + "]";
	}
}
