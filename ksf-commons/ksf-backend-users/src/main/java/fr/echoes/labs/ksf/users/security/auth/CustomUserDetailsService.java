package fr.echoes.labs.ksf.users.security.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import fr.echoes.labs.ksf.users.security.config.AppUserDetails;

/**
 * Authenticate a user from the database.
 */
public class CustomUserDetailsService implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

	private Set<String> admins;

	public CustomUserDetailsService() {
		super();
	}

	/**
	 * @param admins
	 */
	public CustomUserDetailsService(Set<String> admins) {
		super();
		this.admins = admins;
	}

	@Override
	public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
		final String login = token.getPrincipal().toString();
		final String lowercaseLogin = login.toLowerCase();

		LOGGER.debug("Authenticating '{}'", login);
		final List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

		if (this.admins != null && this.admins.contains(lowercaseLogin)) {
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		} else {
			grantedAuthorities.add(new GrantedAuthority() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getAuthority() {
					return "ROLE_USER";
				}
			});
		}

		return new AppUserDetails(lowercaseLogin, grantedAuthorities);
	}
}
