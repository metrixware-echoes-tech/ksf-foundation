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
public class AttachProjectToCustomerCommand {
    @NotBlank
    private String projectKey;
    @NotBlank
    private String customerCode;

    @Override
    public String toString() {
        return "AttachProjectToCustomerCommand{" + "projectKey=" + projectKey + ", customerCode=" + customerCode + '}';
    }

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
     * @return the customerCode
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * @param customerCode the customerCode to set
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
}
