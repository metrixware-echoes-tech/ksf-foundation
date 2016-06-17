/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.echoes.labs.ksf.users.security.auth;

import java.util.HashSet;
import java.util.Set;

import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.ContextSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import fr.echoes.labs.ksf.users.security.config.CasSecurityConfiguration;
import fr.echoes.labs.ksf.users.security.config.LdapSecurityConfiguration;
import fr.echoes.labs.ksf.users.security.config.SecurityConfiguration;

/**
 * This class default initializes the KSF Security Authentication mechanism
 * (ldap, embedded users)...
 *
 * @author sleroy
 */
@Profile("casAuth")
@Service
public class CasUserAuthenticationManager implements IUserAuthenticationManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(CasUserAuthenticationManager.class);

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private LdapSecurityConfiguration ldapSecurity;

	@Autowired
	private SecurityConfiguration security;

	@Autowired
	private CasSecurityConfiguration casSecurityConfiguration;

	@Autowired()
	private ContextSource ldapContextSource;

	/* (non-Javadoc)
	 * @see fr.echoes.labs.ksf.users.security.auth.IUserAuthenticationManager#initializeKsfAuth(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	public void initializeKsfAuth(final AuthenticationManagerBuilder auth) throws Exception {
		// also see LdapAuthenticationProviderConfigurer and @code ContextSourceBuilder

		//		auth.inMemoryAuthentication().withUser("user").password("password1")
		//				.roles("GUI")
		if (this.ldapSecurity.hasConfigurationProvided()) {
			LOGGER.info("Initialization of LDAP Authentication provider");
		}
		LOGGER.info("Initialization of DAO Authentication provider");


		auth.authenticationProvider(casAuthenticationProvider());

		// keep the credentials in the session for using them against a REST API
		auth.eraseCredentials(false);
	}

	private AuthenticationProvider casAuthenticationProvider() {
		final CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
		casAuthenticationProvider.setAuthenticationUserDetailsService(customUserDetailsService());
		casAuthenticationProvider.setServiceProperties(serviceProperties());
		casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
		casAuthenticationProvider.setKey("an_id_for_this_auth_provider_only");
		return casAuthenticationProvider;
	}

	private Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
		return new Cas20ServiceTicketValidator(this.casSecurityConfiguration.getPrefix());
	}

	/* (non-Javadoc)
	 * @see fr.echoes.labs.ksf.users.security.auth.IUserAuthenticationManager#adminList()
	 */
	@Override
	public Set<String> adminList() {
		final Set<String> admins = new HashSet<String>();
		final String adminUserName = "admin";

		admins.add("admin");
		if (adminUserName != null && !adminUserName.isEmpty()) {
			admins.add(adminUserName);
		}


		return admins;
	}

	private AuthenticationUserDetailsService<CasAssertionAuthenticationToken> customUserDetailsService() {
		return new UserDetailsRetrievingService(adminList());
	}

	private ServiceProperties serviceProperties() {
		final ServiceProperties sp = new ServiceProperties();
		sp.setService(this.casSecurityConfiguration.getServiceUrl());
		sp.setSendRenew(false);
		return sp;
	}
}
