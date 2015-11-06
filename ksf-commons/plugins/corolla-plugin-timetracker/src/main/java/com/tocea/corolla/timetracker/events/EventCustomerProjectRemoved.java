/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocea.corolla.timetracker.events;

import com.tocea.corolla.timetracker.domain.Customer;

/**
 *
 * @author sleroy
 */
public class EventCustomerProjectRemoved {
	private Customer customer;
	
	public EventCustomerProjectRemoved(final Customer _customer) {
		super();
		customer = _customer;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public void setCustomer(final Customer _customer) {
		customer = _customer;
	}

	@Override
	public String toString() {
		return "EventCustomerProjectRemoved [customer=" + customer + "]";
	}
}
