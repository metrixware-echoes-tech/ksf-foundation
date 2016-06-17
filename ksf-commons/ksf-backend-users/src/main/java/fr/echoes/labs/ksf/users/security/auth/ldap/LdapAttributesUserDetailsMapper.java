package fr.echoes.labs.ksf.users.security.auth.ldap;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.stereotype.Component;

import com.tocea.corolla.users.domain.User;

import fr.echoes.labs.ksf.users.security.api.PasswordGenerator;
import fr.echoes.labs.ksf.users.security.config.LdapSecurityConfiguration;

@Component
public class LdapAttributesUserDetailsMapper implements AttributesMapper<User> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LdapAttributesUserDetailsMapper.class);

	private final LdapSecurityConfiguration configuration;

	private final PasswordGenerator passwordGenerator;

	@Autowired
	public LdapAttributesUserDetailsMapper(final LdapSecurityConfiguration _configuration,
			final PasswordGenerator _factory) {
		configuration = _configuration;
		passwordGenerator = _factory;

	}

	@Override
	public User mapFromAttributes(final Attributes _attributes) throws NamingException {
		final User user = new User();
		LOGGER.debug("LDAP User informations : {}", _attributes);
		user.setLastName(getStringFromField(_attributes, configuration.field_sn));
		user.setFirstName(getStringFromField(_attributes, configuration.field_givenname));
		user.setLogin(getStringFromField(_attributes, configuration.field_uid));
		user.setActive(true);
		user.setEmail(getStringFromField(_attributes, configuration.field_mail));
		user.setLocaleIfNecessary();
		user.setPassword(passwordGenerator.generatePassword());
		return user;
	}

	private String getStringFromField(final Attributes _attributes, final String _fieldName) {
		
		Object attributeString = null;
		try {
			Validate.notNull(_attributes,"Ldap Attributes should not be empty");
			Validate.notEmpty("Field should not be empty", _fieldName);
			final Attribute attribute = _attributes.get(_fieldName);
			if (attribute != null) {
				attributeString = attribute.get();
			}
		} catch (final NamingException e) {
			LOGGER.debug("LDAP:UserDetails: Could not access to field {}", _fieldName, e);
		}
		return attributeString == null ? "" : attributeString.toString();
	}

}
