/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocea.corolla.timetracker.commands.handlers;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.timetracker.commands.NewActivityCommand;
import com.tocea.corolla.timetracker.dao.IActivityDAO;
import com.tocea.corolla.timetracker.domain.Activity;
import com.tocea.corolla.timetracker.events.EventActivityCreated;
import com.tocea.corolla.timetracker.exceptions.ActivityAlreadyExistingException;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class implements the action to create a new activity.
 * @author sleroy
 */
@CommandHandler
@Transactional
public class NewActivityCommandHandler implements ICommandHandler<NewActivityCommand, Activity> {

    private static final Logger LOG = Logger.getLogger(NewActivityCommandHandler.class.getName());
    @Autowired
    private Gate gate;

    
    @Autowired
    private IActivityDAO activityDao;
    
    @Override
    public Activity handle(@Valid NewActivityCommand command) {
        Activity activityAlreadyExisting = activityDao.findByName(command.getActivityName());
        if (activityAlreadyExisting != null) {
            throw new ActivityAlreadyExistingException(command.getActivityName());
        }
        
        Activity newActivity = new Activity(command.getActivityName());
        activityDao.save(newActivity);
        gate.dispatchEvent(new EventActivityCreated(newActivity));
        return newActivity;
    }

}
