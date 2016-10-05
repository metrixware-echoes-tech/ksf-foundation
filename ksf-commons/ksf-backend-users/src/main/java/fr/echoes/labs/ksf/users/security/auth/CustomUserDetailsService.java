package fr.echoes.labs.ksf.users.security.auth;

import fr.echoes.labs.ksf.users.security.config.AppUserDetails;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Authenticate a user from the database.
 */
public class CustomUserDetailsService implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

    public CustomUserDetailsService() {
        super();
    }

    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
        final String login = token.getPrincipal().toString();

        LOGGER.debug("Authenticating '{}'", login);
        final List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

        grantedAuthorities.add(new GrantedAuthority() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getAuthority() {
                return "ROLE_USER";
            }
        });

        return new AppUserDetails(login, grantedAuthorities);
    }
}
