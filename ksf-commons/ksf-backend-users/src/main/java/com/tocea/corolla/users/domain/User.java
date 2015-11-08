/*
 * Corolla - A Tool to manage software requirements and test cases 
 * Copyright (C) 2015 Tocea
 * 
 * This file is part of Corolla.
 * 
 * Corolla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, 
 * or any later version.
 * 
 * Corolla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Corolla.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tocea.corolla.users.domain;

import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * This class declares a member that contributes to a repository. It is
 * basically identified by the email.
 *
 * @author sleroy
 *
 */
/*@Entity()
 @Table(name = "users", indexes = { @Index(unique = true, name = "login_index", columnList = "login") })*/
@Document(collection = "users")
public class User implements Serializable {

    @Id
    @Field("_id")
    private String id;

    @NotBlank
    @Size(min = 3, max = 30)
    //@Column(nullable = false, length = 30)
    private String login = "";

    @NotBlank
    @Size(max = 40)
    //@Column(nullable = false, length = 40)
    private String firstName = "";

    @NotBlank
    @Size(max = 40)
    //@Column(nullable = false, length = 40)
    private String lastName = "";

    @NotBlank
    @Size(max = 128)
    //@Column(nullable = false, length = 128)
    @Email
    private String email = "";

    @NotBlank
    @Size(max = 256)
    //@Column(nullable = false, length = 256)
    private String password = "";

    @NotNull
    //@Column(nullable = false)
    private String roleId;

    @NotBlank
    @Size(max = 10)
    //@Column(nullable = false, length = 10)
    private String locale = "en_GB";	//$NON-NLS-1$

    @Size(max = 50)
    //@Column(nullable = true, length = 50)
    private String activationToken = "";		//$NON-NLS-1$

    @NotNull
    //@Column(nullable = false)
    private Date createdTime;

    @Size(max = 256)
    //@Column(nullable = false, length = 128)	
    private String salt = "";

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @NotNull
    //@Column(nullable = false)
    private boolean active = true;

    /**
     * Copy values if missing fields
     */
    public void copyMissingFields() {
        if (this.activationToken == null) {
            this.activationToken = "";
        }
        if (Strings.isNullOrEmpty(this.firstName)
                && Strings.isNullOrEmpty(this.lastName)) {
            this.firstName = this.login;
        }
        if (this.createdTime == null) {
            this.createdTime = new Date();
        }

        this.setLocaleIfNecessary();

    }

    /**
     * @return the activationToken
     */
    public String getActivationToken() {
        return this.activationToken;
    }

    /**
     * @return the createdTime
     */
    public Date getCreatedTime() {
        return this.createdTime;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * @return the locale
     */
    public String getLocale() {
        return this.locale;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return this.login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @return the role_id
     */
    public String getRoleId() {
        return this.roleId;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * @param _activationToken the activationToken to set
     */
    public void setActivationToken(final String _activationToken) {
        this.activationToken = _activationToken;
    }

    /**
     * @param _active the active to set
     */
    public void setActive(final boolean _active) {
        this.active = _active;
    }

    /**
     * @param _createdTime the createdTime to set
     */
    public void setCreatedTime(final Date _createdTime) {
        this.createdTime = _createdTime;
    }

    /**
     * @param _email the email to set
     */
    public void setEmail(final String _email) {
        this.email = _email;
    }

    /**
     * @param _firstName the firstName to set
     */
    public void setFirstName(final String _firstName) {
        this.firstName = _firstName;
    }

    /**
     * @param _lastName the lastName to set
     */
    public void setLastName(final String _lastName) {
        this.lastName = _lastName;
    }

    /**
     * @param _locale the locale to set
     */
    public void setLocale(final String _locale) {
        this.locale = _locale;
    }

    /**
     * Set locale if necessary
     *
     * @param _user
     */
    public void setLocaleIfNecessary() {
        if (Strings.isNullOrEmpty(this.getLocale())) {
            this.setLocale(Locale.getDefault().toString());
        }

    }

    /**
     * @param _login the login to set
     */
    public void setLogin(final String _login) {
        this.login = _login;
    }

    /**
     * @param _password the password to set
     */
    public void setPassword(final String _password) {
        this.password = _password;
    }

    public void setRole(final Role _role) {
        this.roleId = _role.getId();
    }

    /**
     * @param _role_id the role_id to set
     */
    public void setRoleId(final String _role_id) {
        this.roleId = _role_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", login=" + login + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", roleId=" + roleId + ", locale=" + locale + ", activationToken=" + activationToken + ", createdTime=" + createdTime + ", active=" + active + '}';
    }

}
