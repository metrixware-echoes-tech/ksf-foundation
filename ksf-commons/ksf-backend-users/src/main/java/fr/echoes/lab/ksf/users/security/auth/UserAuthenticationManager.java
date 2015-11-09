/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.echoes.lab.ksf.users.security.auth;

import fr.echoes.lab.ksf.users.security.config.LdapSecurityConfiguration;
import fr.echoes.lab.ksf.users.security.config.SecurityConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * This class default initializes the KSF Security Authentication mechanism
 * (ldap, embedded users)...
 *
 * @author sleroy
 */
@Service
public class UserAuthenticationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationManager.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private LdapSecurityConfiguration ldapSecurity;

    @Autowired
    private SecurityConfiguration security;

    public void initializeKsfAuth(final AuthenticationManagerBuilder auth) throws Exception {
        // also see LdapAuthenticationProviderConfigurer and @code ContextSourceBuilder
                
        //		auth.inMemoryAuthentication().withUser("user").password("password1")
        //				.roles("GUI")
        if (ldapSecurity.hasConfigurationProvided()) {
            LdapAuthenticationProviderConfigurer<AuthenticationManagerBuilder> ldapAuth = auth.ldapAuthentication();

            LOGGER.info("Enabling LDAP Authentication on server {}", ldapSecurity.getUrl());
            ldapSecurity.clearManagerDN();

            ldapAuth = ldapAuth.contextSource(ldapSecurity.buildLdapContext());

            if (this.ldapSecurity.hasUserDN()) {
                LOGGER.info("Ldap Security base on UserDN pattern");
                ldapAuth = ldapAuth
                        .userDnPatterns(this.ldapSecurity.getUserDnPattern());

            } else if (this.ldapSecurity.hasUserSearch()) {
                LOGGER.info("Ldap Security base on UserDN pattern");
                ldapAuth = ldapAuth
                        .userSearchBase(this.ldapSecurity.getUserSearchBase())
                        .userSearchFilter(this.ldapSecurity.getUserSearchFilter());
            }
            if (!this.ldapSecurity.getGroupSearchBase().isEmpty()) {
                ldapAuth = ldapAuth.groupSearchBase(this.ldapSecurity.getGroupSearchBase());
            }
            if (!this.ldapSecurity.getGroupRoleAttribute().isEmpty()) {
                ldapAuth = ldapAuth.groupRoleAttribute(this.ldapSecurity.getGroupRoleAttribute());
            }
            if (!this.ldapSecurity.getGroupSearchFilter().isEmpty()) {
                ldapAuth = ldapAuth.groupSearchFilter(this.ldapSecurity.getGroupSearchFilter());

            }
        }
        final DaoAuthenticationProvider userEmbeddedAuthenticationProvider = new DaoAuthenticationProvider();
        userEmbeddedAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(this.security.getPasswordStrength()));
        userEmbeddedAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        auth.authenticationProvider(userEmbeddedAuthenticationProvider);

    }
}
