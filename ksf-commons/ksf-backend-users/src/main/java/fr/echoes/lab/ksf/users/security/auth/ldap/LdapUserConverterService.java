package fr.echoes.lab.ksf.users.security.auth.ldap;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Service;

import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.users.commands.CreateUserCommand;
import com.tocea.corolla.users.dao.IUserDAO;
import com.tocea.corolla.users.domain.User;
import com.tocea.corolla.users.dto.UserDto;

import fr.echoes.lab.ksf.users.security.config.LdapSecurityConfiguration;
import fr.echoes.lab.ksf.users.security.utils.SecurityLoggers;

/**
 * This service provides utilities method to convert an Ldap User to a Database
 * user;
 *
 * @author sleroy
 *
 */
@Service
public class LdapUserConverterService {
	private final LdapTemplate ldapTemplate;
	
	private final LdapAttributesUserDetailsMapper userDetailsAttributeMapper;
	
	private final LdapSecurityConfiguration ldapSecurityConfiguration;
	
	private final Gate gate;
	
	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	public LdapUserConverterService(final LdapTemplate _ldapTemplate,
			final LdapAttributesUserDetailsMapper _userDetailsAttributeMapper,
			final LdapSecurityConfiguration _ldapSecurityConfiguration, final Gate _gate) {
		super();
		ldapTemplate = _ldapTemplate;
		userDetailsAttributeMapper = _userDetailsAttributeMapper;
		ldapSecurityConfiguration = _ldapSecurityConfiguration;
		gate = _gate;
	}
	
	public String buildQuery(final String _username) {
		return MessageFormat.format(ldapSecurityConfiguration.getUserDetailsLookup(), _username);
	}
	
	public UserDto convertLdapUserDetailsIntoDatabaseUser(final LdapUserDetails _principal) {
		try {
			final User findUserByLogin = userDAO.findUserByLogin(_principal.getUsername());
			if (findUserByLogin != null) { // User Already existing in database.
				return new UserDto(findUserByLogin);
			}
			final User user = retrieveFromLdap(_principal.getUsername());
			user.setLogin(_principal.getUsername());
			user.copyMissingFields();
			return new UserDto(user);
		} catch (final Exception e) {
			SecurityLoggers.LDAP_LOGGER.error("LDAP : Could not retrieve user informatiosn prior to create a database user for {}", _principal, e);
		}
		final UserDto userDto = new UserDto();
		userDto.setLogin(_principal.getUsername());
		userDto.setFirstName(_principal.getUsername());
		userDto.setEmail(_principal.getUsername()+ "@mail.com");
		userDto.setLastName("");
		userDto.setLocale(Locale.getDefault().toString());
		return userDto;
	}
	
	/**
	 * Try to retrieve informations about an user from the ldap.
	 *
	 * @param _username
	 *            the username
	 * @return the user after beeing reinserted into the database.
	 */
	public User retrieveFromLdap(final String _username) {
		User userLdapInformation = null;
		try {
			final String queryToObtainUser = buildQuery(_username);
			SecurityLoggers.LDAP_LOGGER.debug("Query to obtain user informations {}", queryToObtainUser);
			userLdapInformation = ldapTemplate.lookup(queryToObtainUser, userDetailsAttributeMapper);
			if (userLdapInformation != null) {
				SecurityLoggers.LDAP_LOGGER.info("Found User details from LDAP {}", userLdapInformation);
				gate.dispatch(new CreateUserCommand(userLdapInformation));
			} else {
				SecurityLoggers.LDAP_LOGGER.debug("LDAP did not provide any user information for {}", _username);
			}
		} catch (final Exception e) {
			SecurityLoggers.LDAP_LOGGER.error("LDAP Lookup for {} met an exception", _username, e);
		}
		return userLdapInformation;
		
	}
}
