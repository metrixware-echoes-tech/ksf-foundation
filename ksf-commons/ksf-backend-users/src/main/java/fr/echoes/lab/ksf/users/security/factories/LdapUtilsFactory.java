package fr.echoes.lab.ksf.users.security.factories;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.ContextSource;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.stereotype.Component;

import fr.echoes.lab.ksf.users.security.config.LdapSecurityConfiguration;
import fr.echoes.lab.ksf.users.security.utils.SecurityLoggers;

@Component
public class LdapUtilsFactory {

	@Autowired
	private LdapSecurityConfiguration ldapSecurityConfiguration;

	/**
	 * Returns the ldap context source
	 *
	 * @return the context source.
	 */
	@Bean()
	public DefaultSpringSecurityContextSource ldapContextSource() {
		SecurityLoggers.LDAP_LOGGER.info("Ldap Context source on creation : {}", ldapSecurityConfiguration.getUrl());
		return ldapSecurityConfiguration.buildLdapContext();

	}

	/**
	 * Returns the ldap template
	 *
	 * @return the ldap template
	 */
	@Bean()
	public SpringSecurityLdapTemplate ldapTemplate(final ContextSource _contextSource) {
		SecurityLoggers.LDAP_LOGGER.info("Creating Ldap Template: {}", ldapSecurityConfiguration.getUrl());
		Validate.notNull(ldapSecurityConfiguration);
		return new SpringSecurityLdapTemplate(_contextSource);

	}

}
