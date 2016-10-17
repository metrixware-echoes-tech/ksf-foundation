package fr.echoes.labs.ksf.users.security.auth;

import com.tocea.corolla.users.dao.IRoleDAO;
import com.tocea.corolla.users.dao.IUserDAO;
import com.tocea.corolla.users.domain.Role;
import com.tocea.corolla.users.domain.User;
import com.tocea.corolla.users.dto.AnonymousUserDto;
import com.tocea.corolla.users.dto.UserDto;
import com.tocea.corolla.utils.serviceapi.IReadonlyService;
import fr.echoes.labs.ksf.users.security.api.IAuthenticatedUserService;
import fr.echoes.labs.ksf.users.security.config.AppUserDetails;
import static fr.echoes.labs.ksf.users.security.utils.SecurityLoggers.SECURITY_LOGGER;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Une fois que l'authentification est passée (Credentials vérifiés), ce service
 * est appelé pour récupérer des informations sur l'utilisateur qui est
 * connecté.
 */
@Service("authService")
@Transactional
public class UserDetailsRetrievingService implements UserDetailsService, IReadonlyService, IAuthenticatedUserService, AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

    private static UserDto anonymousUser = new AnonymousUserDto();

    private final IUserDAO userService;

    private final IRoleDAO roleService;

    @Autowired
    public UserDetailsRetrievingService(final IUserDAO _userService, final IRoleDAO _roleService) {
        this.userService = _userService;
        this.roleService = _roleService;
    }

    public UserDetailsRetrievingService() {
        this.userService = null;
        this.roleService = null;
    }

    /**
     * Returns the current logged user.
     *
     * @return the current user.
     */
    @Override
    public UserDto getCurrentUser() {

        try {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            SECURITY_LOGGER.debug("Authentication {}", authentication);
            if (authentication == null) {
                SECURITY_LOGGER.warn("No authentication returned by Spring.");
                return anonymousUser;
            }
            final Object principal = authentication.getPrincipal();
            if (principal instanceof String) {
                return anonymousUser;
            } else if (principal instanceof AuthUser) {
                return new UserDto(((AuthUser) principal).getUser());
            } else if (principal instanceof UserDetails) {
                final UserDetails userDetails = (UserDetails) principal;
                final String username = userDetails.getUsername();

                final UserDto userDto = new UserDto();
                userDto.setId(username);

                userDto.setLogin(username);
                userDto.setFirstName(username);
                userDto.setEmail(username + "@mail.com");
                userDto.setLastName("");
                userDto.setLocale(Locale.getDefault().toString());
                return userDto;
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

        final User user = this.userService.findUserByLogin(username);

        // Try to retrieve informations from the LDAP
        if (user == null) {

            SECURITY_LOGGER.error("Load user by username for " + username + " but user unknown");

            throw new UsernameNotFoundException("User unknown");
        } else if (!user.isActive()) {
            SECURITY_LOGGER.debug("Load user by username for " + username + " but user inactive");

            throw new UsernameNotFoundException("User inactive");
        }
        Role role = this.roleService.findOne(user.getRoleId());
        if (role == null) {
            SECURITY_LOGGER.debug("User {} does not have role!", username);
            role = Role.DEFAULT_ROLE;
        }
        SECURITY_LOGGER.info("User {} role {}", username, role);
        authUser = new AuthUser(user, role);
        SECURITY_LOGGER.debug("Informations returned {}", authUser);

        return authUser;
    }

    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) {
        final String login = token.getPrincipal().toString();

        SECURITY_LOGGER.debug("Authenticating '{}'", login);
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
