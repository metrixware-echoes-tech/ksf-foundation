/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocea.corolla.timetracker.commands;

import com.tocea.corolla.timetracker.domain.Customer;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sleroy
 */
public class EditCustomerDetailsCommand {

    @NotNull
    private Customer customerDto;

    /**
     * @return the customerDto
     */
    public Customer getCustomerDto() {
        return customerDto;
    }

    /**
     * @param customerDto the customerDto to set
     */
    public void setCustomerDto(Customer customerDto) {
        this.customerDto = customerDto;
    }
}
