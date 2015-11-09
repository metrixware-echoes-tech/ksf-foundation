package fr.echoes.lab.ksf.users.security.auth.ldap;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.users.domain.User;

import fr.echoes.lab.ksf.users.security.auth.PasswordGeneratorService;
import fr.echoes.lab.ksf.users.security.config.LdapSecurityConfiguration;
import fr.echoes.lab.ksf.users.security.config.SecurityConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class LdapUserConverterServiceTest {
	private final SecurityConfiguration		securityConfiguration	= new SecurityConfiguration();
	private final LdapSecurityConfiguration	ldapConfiguration		= new LdapSecurityConfiguration();
	private final PasswordGeneratorService		passwordGeneratorService	= new PasswordGeneratorService(securityConfiguration);
	@Mock
	private Gate gate;

	@Test
	public final void testRetrieveFromLdap() throws Exception {

		ldapConfiguration.setUrl("ldap://ares");
		ldapConfiguration.setRoot("dc=tocea,dc=com");
		ldapConfiguration.setManagerDn("");
		ldapConfiguration.setManagerPassword("");
		ldapConfiguration.setPooled(false);
		ldapConfiguration.setUserDnPattern("uid={0},ou=people");
		ldapConfiguration.setGroupSearchBase("ou=groups");

		//
		final LdapAttributesUserDetailsMapper attributeManager = new LdapAttributesUserDetailsMapper(ldapConfiguration,
				passwordGeneratorService);

		final DefaultSpringSecurityContextSource context = ldapConfiguration.buildLdapContext();
		assertNotNull(context);

		final LdapTemplate ldapTemplate = new LdapTemplate(context);
		ldapTemplate.afterPropertiesSet();

		final LdapUserConverterService userDetailsRetrievingService = new LdapUserConverterService(ldapTemplate, attributeManager, ldapConfiguration, gate);
		final User ldapUser = userDetailsRetrievingService.retrieveFromLdap("sleroy");
		System.out.println(ldapUser);

	}

}
