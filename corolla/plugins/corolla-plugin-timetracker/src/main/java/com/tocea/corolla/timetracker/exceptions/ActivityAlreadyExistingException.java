/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocea.corolla.timetracker.exceptions;

import com.tocea.corolla.utils.domain.CorollaDomainException;

/**
 *
 * @author sleroy
 */
public class ActivityAlreadyExistingException extends CorollaDomainException {

    public ActivityAlreadyExistingException(String activityName) {
        super("Activity "+ activityName + " is already existing");
    }
    
}
