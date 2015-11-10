package fr.echoes.lab.ksf.users.security.auth.ldap;

import org.junit.rules.ExternalResource;
import org.springframework.security.ldap.server.ApacheDSContainer;

public class JUnitLdapRule extends ExternalResource {
	public static final int		LOCAL_PORT	= 33389;
	public static final String	ROOT			= "dc=springframework,dc=org";
	public static final String	MANAGER_DN	= "";
	public static final String	MANAGER_PWD	= "";
	private ApacheDSContainer	apacheDsContainer;
	
	public String getUrl() {
		return "ldap://localhost";
	}
	
	@Override
	protected void after() {
		try {
			apacheDsContainer.destroy();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void before() throws Throwable {
		apacheDsContainer = new ApacheDSContainer(ROOT, "classpath:ldap/test_ldap.ldif");
		apacheDsContainer.setPort(LOCAL_PORT);
		apacheDsContainer.afterPropertiesSet();
	}
}
