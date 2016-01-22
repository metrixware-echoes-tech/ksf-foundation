package fr.echoes.lab.ksf.cc.health;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Component;

@Component
public class LdapHealthIndicator extends AbstractHealthIndicator {

	@Autowired
	private LdapTemplate ldapTemplate;
	
	@Override
	protected void doHealthCheck(Builder builder) throws Exception {
		
		try {
			
			List userList = ldapTemplate.search("", "(objectclass=person)", new AttributesMapper() {
	            public Object mapFromAttributes(Attributes attrs)
	               throws NamingException {
	               return attrs.get("cn").get();
	            }
			});
			
			if (userList == null || userList.isEmpty()) {
				throw new Exception("Cannot find any user in the LDAP repository");
			}
			
			builder.up();
			
		} catch (Exception ex) {
			
			builder.down().withException(ex);
		}
		
	}
	
}
