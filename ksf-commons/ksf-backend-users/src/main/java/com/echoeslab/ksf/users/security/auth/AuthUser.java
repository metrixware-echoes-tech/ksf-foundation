package com.echoeslab.ksf.users.security.auth;

import com.tocea.corolla.users.domain.Role;
import com.tocea.corolla.users.domain.User;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

public class AuthUser extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = -9086733140310198830L;
    private User user;

    public AuthUser(final String username, final String password, final boolean enabled, final boolean accountNonExpired, final boolean credentialsNonExpired, final boolean accountNonLocked, final Collection<GrantedAuthority> authorities) throws IllegalArgumentException {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public AuthUser(final User user, final Role _role) {
        this(user.getLogin(), user.getPassword(), true, true, true, true, _role.getGrantedAuthorities());

        this.user = user;
    }

    public Object getSalt() {
        return this.user != null ? this.user.getSalt() : null;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(final User user) {
        this.user = user;
    }
    private static final Logger LOG = LoggerFactory.getLogger(AuthUser.class.getName());
}
