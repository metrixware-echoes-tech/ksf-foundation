package fr.echoes.lab.ksf.users.security.auth;

import static fr.echoes.lab.ksf.users.security.utils.SecurityLoggers.SECURITY_LOGGER;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Service;

import com.tocea.corolla.users.dao.IRoleDAO;
import com.tocea.corolla.users.dao.IUserDAO;
import com.tocea.corolla.users.domain.Role;
import com.tocea.corolla.users.domain.User;
import com.tocea.corolla.users.dto.AnonymousUserDto;
import com.tocea.corolla.users.dto.UserDto;
import com.tocea.corolla.utils.serviceapi.IReadonlyService;

import fr.echoes.lab.ksf.users.security.api.IAuthenticatedUserService;
import fr.echoes.lab.ksf.users.security.auth.ldap.LdapUserConverterService;

/**
 * Une fois que l'authentification est passée (Credentials vérifiés), ce service
 * est appelé pour récupérer des informations sur l'utilisateur qui est
 * connecté.
 */
@Service("authService")
@Transactional
public class UserDetailsRetrievingService implements UserDetailsService, IReadonlyService,  IAuthenticatedUserService {
	
	private static UserDto anonymousUser = new AnonymousUserDto();
	
	private final IUserDAO userService;
	
	private final IRoleDAO roleService;

	private final LdapUserConverterService ldapUserConverterService;

	

	
	@Autowired
	public UserDetailsRetrievingService(final IUserDAO _userService, final IRoleDAO _roleService,final LdapUserConverterService _ldapUserConverterService) {
		userService = _userService;
		roleService = _roleService;
		ldapUserConverterService = _ldapUserConverterService;

	}

	
	
	/**
	 * Returns the current logged user.
	 *
	 * @return the current user.
	 */
	public UserDto getCurrentUser() {
		
		try {
			final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			SECURITY_LOGGER.debug("Authentication {}", authentication);
			if (authentication == null ) {
				SECURITY_LOGGER.warn("No authentication returned by Spring.");
				return anonymousUser;
			}
			final Object principal = authentication.getPrincipal();
			if (principal instanceof String) {
				return anonymousUser;
			} else if (principal instanceof LdapUserDetails) {
				// Conversion de l'utilisateur LDAP et retour comme BddUser
				return ldapUserConverterService.convertLdapUserDetailsIntoDatabaseUser((LdapUserDetails) principal);
			} else if (principal instanceof AuthUser) {
				return new UserDto(((AuthUser) principal).getUser());
			}
		} catch (final Exception e) {
			SECURITY_LOGGER.error("Cannot retrieve AuthUser : ANONYMOUS CREDENTIALS ARE PROVIDED : ", e);
		}
		
		return anonymousUser;
		
	}
	
	@Override
	public String getCurrentUserLogin() {
		return getCurrentUser().getLogin();
	}
	
	
	
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		
		SECURITY_LOGGER.debug("Requesting information about {}", username);
		final AuthUser authUser;
		
		final User user = userService.findUserByLogin(username);
		
		// Try to retrieve informations from the LDAP
		if (user == null) {
			
			SECURITY_LOGGER.error("Load user by username for " + username + " but user unknown");
			
			throw new UsernameNotFoundException("User unknown");
		} else if (!user.isActive()) {
			SECURITY_LOGGER.debug("Load user by username for " + username + " but user inactive");
			
			throw new UsernameNotFoundException("User inactive");
		}
		Role role = roleService.findOne(user.getRoleId());
		if (role == null) {
			SECURITY_LOGGER.debug("User {} does not have role!", username);
			role = Role.DEFAULT_ROLE;
		}
		SECURITY_LOGGER.info("User {} role {}", username, role);
		authUser = new AuthUser(user, role);
		SECURITY_LOGGER.debug("Informations returned {}", authUser);
		
		return authUser;
	}
	
}
