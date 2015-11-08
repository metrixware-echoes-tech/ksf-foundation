/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocea.corolla.timetracker.events;

import com.tocea.corolla.timetracker.domain.TimesheetEntry;

/**
 * This event is launched when the time reported on a timeserie has changed.
 * @author sleroy
 */
public class EventTimesheetEntryHoursReported {
	private TimesheetEntry timesheetEntry;
	
	public EventTimesheetEntryHoursReported(final TimesheetEntry _timesheetEntry) {
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
		return "EventTimesheetEntryHoursReported [timesheetEntry=" + timesheetEntry + "]";
	}
}
