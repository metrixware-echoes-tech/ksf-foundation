package com.echoeslab.ksf.cc.ui.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.tocea.corolla.users.domain.Role;
import com.tocea.corolla.users.domain.User;

public class AuthUser extends org.springframework.security.core.userdetails.User
{
	private static final long serialVersionUID = -9086733140310198830L;
	private	User		user;


	public AuthUser(final String username, final String password, final boolean enabled, final boolean accountNonExpired, final boolean credentialsNonExpired, final boolean accountNonLocked, final Collection<GrantedAuthority> authorities) throws IllegalArgumentException
	{
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	public AuthUser(final User user, final Role _role)
	{
		this(user.getLogin(), user.getPassword(), true, true, true, true,_role.getGrantedAuthorities());

		this.user = user;
	}

	public Object getSalt()
	{
		return this.user != null ? this.user.getSalt() : null;
	}

	public User getUser()
	{
		return this.user;
	}

	public void setUser(final User user)
	{
		this.user = user;
	}
}
