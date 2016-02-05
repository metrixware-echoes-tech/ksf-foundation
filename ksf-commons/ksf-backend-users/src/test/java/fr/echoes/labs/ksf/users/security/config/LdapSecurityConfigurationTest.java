package fr.echoes.labs.ksf.users.security.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;

public class LdapSecurityConfigurationTest {

	private final LdapSecurityConfiguration configuration = new LdapSecurityConfiguration();

	@Test
	public void testBuildLdapContext() throws Exception {
		configuration.setUrl("ldap://ares");
		configuration.setRoot("dc=tocea,dc=com");
		configuration.setManagerDn("");
		configuration.setManagerPassword("");
		configuration.setPooled(true);
		configuration.setUserDnPattern("uid={0},ou=people");
		configuration.setGroupSearchBase("ou=groups");
		configuration.buildLdapContext();
		
	}



	@Test
	@Ignore("Test pour vérifier la connectivité à un LDAP / Debug only")
	public final void testBuildLdapContext_Ares() throws Exception {
		configuration.setUrl("ldap://ares");
		configuration.setRoot("dc=tocea,dc=com");
		configuration.setManagerDn("");
		configuration.setManagerPassword("");
		configuration.setPooled(false);
		configuration.setUserDnPattern("uid={0},ou=people");
		configuration.setGroupSearchBase("ou=groups");
		final DefaultSpringSecurityContextSource context = configuration.buildLdapContext();
		assertNotNull(context);
		//System.out.println(configuration);
		
		final SpringSecurityLdapTemplate springSecurityLdapTemplate = new SpringSecurityLdapTemplate(context);
		springSecurityLdapTemplate.afterPropertiesSet();
		final List<String> search = springSecurityLdapTemplate.search(query().where("objectclass").is("person"), new AttributesMapper<String>() {

			@Override
			public String mapFromAttributes(final Attributes _attributes) throws NamingException {
				System.out.println(_attributes);
				return _attributes.get("cn").get().toString();
			}
		});
		System.out.println(search);
	}

	@Test
	public final void testClearManagerDN() throws Exception {
		// TODO
		// throw new RuntimeException("not yet implemented");
	}

	@Test
	public final void testGetProviderUrl() throws Exception {
		configuration.setUrl("ldap://localhost");
		configuration.setPort(389);
		configuration.setRoot("dc=tocea,dc=com");

		assertEquals("ldap://localhost:389/dc=tocea,dc=com", configuration.getProviderUrl());

	}

	@Test
	public final void testHasConfigurationProvided() throws Exception {
		configuration.setUrl("");
		assertFalse(configuration.hasConfigurationProvided());
		configuration.setUrl("ldap://");
		assertTrue(configuration.hasConfigurationProvided());

	}

	@Test
	public final void testHasUserDN() throws Exception {
		configuration.setUserDnPattern("");
		assertFalse(configuration.hasUserDN());
		configuration.setUserDnPattern("gni");
		assertTrue(configuration.hasUserDN());
	}

	@Test
	public final void testHasUserSearch() throws Exception {
		// TODO
		// throw new RuntimeException("not yet implemented");
	}
	
	
	
	@Test
	public final void testIsAuthRequired() throws Exception {
		// TODO
		// throw new RuntimeException("not yet implemented");
	}

}
