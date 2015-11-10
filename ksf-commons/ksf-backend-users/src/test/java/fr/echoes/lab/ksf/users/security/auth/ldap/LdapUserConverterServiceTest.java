package fr.echoes.lab.ksf.users.security.auth.ldap;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Rule;
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

	private final SecurityConfiguration		securityConfiguration		= new SecurityConfiguration();
	private final LdapSecurityConfiguration	ldapConfiguration			= new LdapSecurityConfiguration();
	private final PasswordGeneratorService	passwordGeneratorService	= new PasswordGeneratorService(
			securityConfiguration);
	@Mock
	private Gate							gate;

	@Rule
	public final JUnitLdapRule	ldapRule	= new JUnitLdapRule();
	private LdapTemplate		ldapTemplate;
	private LdapAttributesUserDetailsMapper attributeManager;

	@Before
	public void before() throws Exception {
		ldapConfiguration.setUrl(ldapRule.getUrl());
		ldapConfiguration.setRoot(JUnitLdapRule.ROOT);
		ldapConfiguration.setManagerDn(JUnitLdapRule.MANAGER_DN);
		ldapConfiguration.setManagerPassword(JUnitLdapRule.MANAGER_PWD);
		ldapConfiguration.setPort(JUnitLdapRule.LOCAL_PORT);
		;
		ldapConfiguration.setPooled(false);
		ldapConfiguration.setUserDetailsLookup("uid={0},ou=people");
		ldapConfiguration.setGroupSearchBase("ou=groups");

		attributeManager = new LdapAttributesUserDetailsMapper(ldapConfiguration,
				passwordGeneratorService);

		final DefaultSpringSecurityContextSource context = ldapConfiguration.buildLdapContext();
		assertNotNull(context);
		ldapTemplate = new LdapTemplate(context);
		ldapTemplate.afterPropertiesSet();
	}

	@Test
	public final void testRetrieveFromLdap() throws Exception {

		final LdapUserConverterService userDetailsRetrievingService = new LdapUserConverterService(ldapTemplate,
				attributeManager, ldapConfiguration, gate);
		final User ldapUser = userDetailsRetrievingService.retrieveFromLdap("bob");
		assertNotNull(ldapUser);

	}

	@Test
	public final void testRetrieveFromLdapWithoutUser() throws Exception {
		final LdapUserConverterService userDetailsRetrievingService = new LdapUserConverterService(ldapTemplate,
				attributeManager, ldapConfiguration, gate);
		final User ldapUser2 = userDetailsRetrievingService.retrieveFromLdap("gnignagnu");
		assertNull(ldapUser2);

	}

}
