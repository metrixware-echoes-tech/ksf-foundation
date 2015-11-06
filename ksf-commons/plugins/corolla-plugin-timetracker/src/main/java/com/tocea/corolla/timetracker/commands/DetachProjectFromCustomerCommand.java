/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocea.corolla.timetracker.commands;

import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author sleroy
 */
public class DetachProjectFromCustomerCommand {

    @NotBlank
    private String projectKey;
    @NotBlank
    private String customerName;

    /**
     * @return the projectKey
     */
    public String getProjectKey() {
        return projectKey;
    }

    /**
     * @param projectKey the projectKey to set
     */
    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
