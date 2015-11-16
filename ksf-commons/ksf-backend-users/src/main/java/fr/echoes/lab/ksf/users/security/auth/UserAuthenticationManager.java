/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.echoes.lab.ksf.users.security.auth;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.echoes.lab.ksf.users.security.config.LdapSecurityConfiguration;
import fr.echoes.lab.ksf.users.security.config.SecurityConfiguration;
import fr.echoes.lab.ksf.users.security.utils.SecurityLoggers;

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
	
	@Autowired()
	private ContextSource ldapContextSource;

	public void initializeKsfAuth(final AuthenticationManagerBuilder auth) throws Exception {
		// also see LdapAuthenticationProviderConfigurer and @code ContextSourceBuilder
		
		//		auth.inMemoryAuthentication().withUser("user").password("password1")
		//				.roles("GUI")
		if (ldapSecurity.hasConfigurationProvided()) {
			LOGGER.info("Initialization of LDAP Authentication provider");
			initializeLdapAuthenticationManager(auth);
		}
		LOGGER.info("Initialization of DAO Authentication provider");
		final DaoAuthenticationProvider userEmbeddedAuthenticationProvider = new DaoAuthenticationProvider();
		userEmbeddedAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(security.getPasswordStrength()));
		userEmbeddedAuthenticationProvider.setUserDetailsService(userDetailsService);
		auth.authenticationProvider(userEmbeddedAuthenticationProvider);

		// keep the credentials in the session for using them against a REST API
		auth.eraseCredentials(false);
	}
	
	private void initializeLdapAuthenticationManager(final AuthenticationManagerBuilder auth) throws Exception {
		LdapAuthenticationProviderConfigurer<AuthenticationManagerBuilder> ldapAuth = auth.ldapAuthentication();

		SecurityLoggers.LDAP_LOGGER.info("Enabling LDAP Authentication on server {}", ldapSecurity.getUrl());
		ldapSecurity.clearManagerDN();
		Validate.notNull(ldapContextSource);
		ldapAuth = ldapAuth.contextSource((BaseLdapPathContextSource) ldapContextSource);

		if (ldapSecurity.hasUserDN()) {
			SecurityLoggers.LDAP_LOGGER.info("Ldap Security base on UserDN pattern");
			ldapAuth = ldapAuth
					.userDnPatterns(ldapSecurity.getUserDnPattern());

		} else if (ldapSecurity.hasUserSearch()) {
			SecurityLoggers.LDAP_LOGGER.info("Ldap Security base on UserDN pattern");
			ldapAuth = ldapAuth
					.userSearchBase(ldapSecurity.getUserSearchBase())
					.userSearchFilter(ldapSecurity.getUserSearchFilter());
		}
		if (!ldapSecurity.getGroupSearchBase().isEmpty()) {
			SecurityLoggers.LDAP_LOGGER.info("Setting groupSearchBase={}", ldapSecurity.getGroupSearchBase());
			ldapAuth = ldapAuth.groupSearchBase(ldapSecurity.getGroupSearchBase());
		}
		if (!ldapSecurity.getGroupRoleAttribute().isEmpty()) {
			SecurityLoggers.LDAP_LOGGER.info("Setting groupRoleAttribute={}", ldapSecurity.getGroupRoleAttribute());
			ldapAuth = ldapAuth.groupRoleAttribute(ldapSecurity.getGroupRoleAttribute());
		}
		if (!ldapSecurity.getGroupSearchFilter().isEmpty()) {
			SecurityLoggers.LDAP_LOGGER.info("Setting groupSearchFilter={}", ldapSecurity.getGroupSearchFilter());
			ldapAuth = ldapAuth.groupSearchFilter(ldapSecurity.getGroupSearchFilter());

		}
		
	}
}
