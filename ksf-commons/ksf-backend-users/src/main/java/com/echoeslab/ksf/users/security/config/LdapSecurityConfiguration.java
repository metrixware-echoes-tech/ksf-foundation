/**
 *
 */
package com.echoeslab.ksf.users.security.config;

import groovy.transform.Canonical;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.stereotype.Component;

/**
 * This class defines the security configuration for Corolla;
 *
 * @author sleroy
 *
 */
@Component
@ConfigurationProperties(prefix = "ksf.security.ldap")
@Canonical
public class LdapSecurityConfiguration {

    private String url;

    private String managerDn = "";

    @Override
    public String toString() {
        return "LdapSecurityConfiguration{" + "url=" + url + ", managerDn=" + managerDn + ", managerPassword=" + managerPassword + ", root=" + root + ", port=" + port + ", userDnPattern=" + userDnPattern + ", userSearchFilter=" + userSearchFilter + ", userSearchBase=" + userSearchBase + ", groupSearchBase=" + groupSearchBase + ", groupRoleAttribute=" + groupRoleAttribute + ", groupSearchFilter=" + groupSearchFilter + '}';
    }

    private String managerPassword = "";

    private String root = "dc=springframework,dc=org";

    private Integer port = 33389;

    private String userDnPattern = "";

    private String userSearchFilter = "uid={0},ou=people";

    private String userSearchBase = "";

    private String groupSearchBase = "ou=groups";

    private String groupRoleAttribute = "";

    private String groupSearchFilter = "";

    public boolean hasUserDN() {
        return !userDnPattern.isEmpty();
    }

    public boolean hasUserSearch() {
        return !userSearchBase.isEmpty();
    }

    /**
     * Vérifie si une URL est fournie pour LDAP. Si pas d'URL, pas
     * d'authentification.
     */
    public boolean hasConfigurationProvided() {
        return url != null && !url.isEmpty();
    }

    /**
     * Vérifie si l'authentification est requise dans l'application
     * (user,password)
     */
    public boolean isAuthRequired() {
        return managerDn != null;
    }

    /**
     * Le ManagerDN doit être à null pour activer l'accès anonymous.
     */
    public void clearManagerDN() {
        if (managerDn == "") {
            managerDn = null;
        }
    }

    public String getProviderUrl() {
        if (url == null) {
            return "<undefined>";
        }
        return url + ":" + port + "/" + root;

    }

    public DefaultSpringSecurityContextSource buildLdapContext() {
        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(getProviderUrl());
        if (managerDn != null) {
            contextSource.setUserDn(managerDn);
            if (managerPassword == null) {
                throw new IllegalStateException("managerPassword is required if managerDn is supplied");
            }
            contextSource.setPassword(managerPassword);
        }
        return contextSource;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getManagerDn() {
        return managerDn;
    }

    public void setManagerDn(String managerDn) {
        this.managerDn = managerDn;
    }

    public String getManagerPassword() {
        return managerPassword;
    }

    public void setManagerPassword(String managerPassword) {
        this.managerPassword = managerPassword;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserDnPattern() {
        return userDnPattern;
    }

    public void setUserDnPattern(String userDnPattern) {
        this.userDnPattern = userDnPattern;
    }

    public String getUserSearchFilter() {
        return userSearchFilter;
    }

    public void setUserSearchFilter(String userSearchFilter) {
        this.userSearchFilter = userSearchFilter;
    }

    public String getUserSearchBase() {
        return userSearchBase;
    }

    public void setUserSearchBase(String userSearchBase) {
        this.userSearchBase = userSearchBase;
    }

    public String getGroupSearchBase() {
        return groupSearchBase;
    }

    public void setGroupSearchBase(String groupSearchBase) {
        this.groupSearchBase = groupSearchBase;
    }

    public String getGroupRoleAttribute() {
        return groupRoleAttribute;
    }

    public void setGroupRoleAttribute(String groupRoleAttribute) {
        this.groupRoleAttribute = groupRoleAttribute;
    }

    public String getGroupSearchFilter() {
        return groupSearchFilter;
    }

    public void setGroupSearchFilter(String groupSearchFilter) {
        this.groupSearchFilter = groupSearchFilter;
    }
}
