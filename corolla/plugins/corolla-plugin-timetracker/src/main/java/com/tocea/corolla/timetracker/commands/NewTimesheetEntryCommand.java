/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocea.corolla.timetracker.commands;

import com.tocea.corolla.timetracker.domain.TimesheetEntry;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sleroy
 */
public class NewTimesheetEntryCommand {
    @NotNull
    private TimesheetEntry timesheetEntry;

    /**
     * @return the timesheetEntry
     */
    public TimesheetEntry getTimesheetEntry() {
        return timesheetEntry;
    }

    /**
     * @param timesheetEntry the timesheetEntry to set
     */
    public void setTimesheetEntry(TimesheetEntry timesheetEntry) {
        this.timesheetEntry = timesheetEntry;
    }
}
