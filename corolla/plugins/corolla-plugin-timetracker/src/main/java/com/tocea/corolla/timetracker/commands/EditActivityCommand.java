/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocea.corolla.timetracker.commands;

import com.tocea.corolla.timetracker.domain.Activity;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sleroy
 */
public class EditActivityCommand {
    @NotNull
    private Activity activityDto;

    /**
     * @return the activityDto
     */
    public Activity getActivityDto() {
        return activityDto;
    }

    /**
     * @param activityDto the activityDto to set
     */
    public void setActivityDto(Activity activityDto) {
        this.activityDto = activityDto;
    }
}
