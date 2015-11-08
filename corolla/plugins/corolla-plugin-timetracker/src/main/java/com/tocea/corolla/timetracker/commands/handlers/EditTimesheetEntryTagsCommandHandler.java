/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocea.corolla.timetracker.commands.handlers;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.timetracker.domain.Customer;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author sleroy
 */
@CommandHandler
@Transactional
public class EditTimesheetEntryTagsCommandHandler implements ICommandHandler<EditTimesheetEntryTagsCommandHandler, Customer> {

    @Autowired
    private Gate gate;

    @Override
    public Customer handle(@Valid EditTimesheetEntryTagsCommandHandler command) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
