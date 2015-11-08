/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocea.corolla.timetracker.commands;

import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sleroy
 */
public class EditTimesheetEntryTagsCommand {

    private List<String> tags;
    @NotNull
    private Integer timesheetEntryId;

    /**
     * @return the tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

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
}
