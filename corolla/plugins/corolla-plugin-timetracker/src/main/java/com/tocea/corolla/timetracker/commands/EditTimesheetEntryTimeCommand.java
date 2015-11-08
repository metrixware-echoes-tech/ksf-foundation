/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocea.corolla.timetracker.commands;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sleroy
 */
public class EditTimesheetEntryTimeCommand {

    @NotNull
    private Integer timesheetEntryId;
    @Min(0L)
    @NotNull
    private Float hours;

    /**
     * @return the timesheetEntryId
     */
    public Integer getTimesheetEntryId() {
        return timesheetEntryId;
    }

    /**
     * @param timesheetEntryId the timesheetEntryId to set
     */
    public void setTimesheetEntryId(Integer timesheetEntryId) {
        this.timesheetEntryId = timesheetEntryId;
    }

    /**
     * @return the hours
     */
    public Float getHours() {
        return hours;
    }

    /**
     * @param hours the hours to set
     */
    public void setHours(Float hours) {
        this.hours = hours;
    }
}
