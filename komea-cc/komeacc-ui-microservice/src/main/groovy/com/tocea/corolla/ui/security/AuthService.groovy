package com.tocea.corolla.ui.security

import groovy.util.logging.Slf4j

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

import com.tocea.corolla.users.dao.IRoleDAO
import com.tocea.corolla.users.dao.IUserDAO
import com.tocea.corolla.users.domain.Role
import com.tocea.corolla.users.domain.User
import com.tocea.corolla.users.dto.UserDto;
import com.tocea.corolla.utils.serviceapi.IReadonlyService

/**
 * Provides userDetails
 */
@Service("authService")
@Slf4j
class AuthService implements UserDetailsService, IReadonlyService {

	@Autowired
	def IUserDAO			userService

	@Autowired
	def IRoleDAO			roleService

	@Override
	public UserDetails loadUserByUsername(final String username)
	throws UsernameNotFoundException {
		
		log.debug("Requesting information about {}", username)
		final AuthUser authUser

		final User user = this.userService.findUserByLogin(username)

		if (user == null ) {
			log.debug("Load user by username for " + username
					+ " but user unknown")

			throw new UsernameNotFoundException("User unknown")
		} else if (!user.isActive()) {
			log.debug("Load user by username for " + username
					+ " but user inactive")

			throw new UsernameNotFoundException("User inactive")
		}
		Role role = this.roleService.findOne(user.getRoleId())
		if (role == null) {
			log.debug("User {} does not have role!", username)
			role = Role.DEFAULT_ROLE
		}
		log.info("User {} role {}", username, role)
		authUser = new AuthUser(user, role)
		log.debug("Informations returned {}", authUser)
		
		return authUser
	}
	
	public UserDto getCurrentUser() {
		
		AuthUser userDetails;
		
		try {
			userDetails = (AuthUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			log.debug("Cannot retrieve AuthUser.")
		}

		return userDetails != null ? new UserDto(userDetails.getUser()) : null
		
	}
}