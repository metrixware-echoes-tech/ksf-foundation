package fr.echoes.lab.ksf.users.security.auth;

import com.tocea.corolla.users.dao.IRoleDAO;
import com.tocea.corolla.users.dao.IUserDAO;
import com.tocea.corolla.users.domain.Role;
import com.tocea.corolla.users.domain.User;
import com.tocea.corolla.users.dto.UserDto;
import com.tocea.corolla.utils.serviceapi.IReadonlyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Provides userDetails
 */
@Service("authService")
public class AuthService implements UserDetailsService, IReadonlyService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class.getName());

    @Autowired
    private IUserDAO userService;

    @Autowired
    private IRoleDAO roleService;

    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {

        LOG.debug("Requesting information about {}", username);
        final AuthUser authUser;

        final User user = this.getUserService().findUserByLogin(username);

        if (user == null) {
            LOG.debug("Load user by username for " + username + " but user unknown");

            throw new UsernameNotFoundException("User unknown");
        } else if (!user.isActive()) {
            LOG.debug("Load user by username for " + username
                    + " but user inactive");

            throw new UsernameNotFoundException("User inactive");
        }
        Role role = this.getRoleService().findOne(user.getRoleId());
        if (role == null) {
            LOG.debug("User {} does not have role!", username);
            role = Role.DEFAULT_ROLE;
        }
        LOG.info("User {} role {}", username, role);
        authUser = new AuthUser(user, role);
        LOG.debug("Informations returned {}", authUser);

        return authUser;
    }

    public UserDto getCurrentUser() {

        AuthUser userDetails = null;

        try {
            userDetails = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            LOG.debug("Cannot retrieve AuthUser.");
        }

        return userDetails != null ? new UserDto(userDetails.getUser()) : null;

    }

    /**
     * @return the userService
     */
    public IUserDAO getUserService() {
        return userService;
    }

    /**
     * @return the roleService
     */
    public IRoleDAO getRoleService() {
        return roleService;
    }

    /**
     * @param roleService the roleService to set
     */
    public void setRoleService(IRoleDAO roleService) {
        this.roleService = roleService;
    }
}
