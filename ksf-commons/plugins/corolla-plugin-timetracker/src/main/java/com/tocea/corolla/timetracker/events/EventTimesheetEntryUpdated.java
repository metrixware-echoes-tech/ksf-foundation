/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocea.corolla.timetracker.events;

import com.tocea.corolla.timetracker.domain.TimesheetEntry;

/**
 *
 * @author sleroy
 */
public class EventTimesheetEntryUpdated {
	private TimesheetEntry timesheetEntry;
	
	public EventTimesheetEntryUpdated(final TimesheetEntry _timesheetEntry) {
		super();
		timesheetEntry = _timesheetEntry;
	}
	
	

	/**
	 * @return the timesheetEntry
	 */
	public TimesheetEntry getTimesheetEntry() {
		return timesheetEntry;
	}
	
	/**
	 * @param timesheetEntry
	 *            the timesheetEntry to set
	 */
	public void setTimesheetEntry(final TimesheetEntry timesheetEntry) {
		this.timesheetEntry = timesheetEntry;
	}
	
	@Override
	public String toString() {
		return "EventTimesheetEntryUpdated [timesheetEntry=" + timesheetEntry + "]";
	}
}
