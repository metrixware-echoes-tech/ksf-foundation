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
public class EventActivityDetailsUpdated {
	private Activity activity;
	
	public EventActivityDetailsUpdated(final Activity _activity) {
		super();
		activity = _activity;
	}
	
	public Activity getActivity() {
		return activity;
	}
	
	public void setActivity(final Activity _activity) {
		activity = _activity;
	}

	@Override
	public String toString() {
		return "EventActivityDetailsUpdated [activity=" + activity + "]";
	}
}
