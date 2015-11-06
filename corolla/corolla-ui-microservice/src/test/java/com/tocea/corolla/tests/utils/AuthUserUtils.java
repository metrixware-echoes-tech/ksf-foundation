package com.tocea.corolla.tests.utils;

import com.tocea.corolla.ui.security.AuthUser;
import com.tocea.corolla.users.domain.Permission;
import com.tocea.corolla.users.domain.Role;
import com.tocea.corolla.users.domain.User;

public class AuthUserUtils {

	public static AuthUser basicUser() {
		
		Role basicRole = new Role();
		basicRole.setName("BASIC");
		basicRole.setPermissions("");
		
		User basicUser = new User();
		basicUser.setLogin("simple");
		basicUser.setPassword("pass");
		basicUser.setEmail("simple@corolla.com");
		
		return new AuthUser(basicUser, basicRole);
	}
	
	public static AuthUser portfolioManager() {
		
		Role portfolioManagerRole = new Role();
		portfolioManagerRole.setName("PORTFOLIO MANAGER");
		portfolioManagerRole.setPermissions(Permission.PORTFOLIO_MANAGEMENT);

		User portfolioManagerUser = new User();
		portfolioManagerUser.setLogin("portfolio_manager");
		portfolioManagerUser.setPassword("pass");
		portfolioManagerUser.setEmail("manager@corolla.com");
		
		return new AuthUser(portfolioManagerUser, portfolioManagerRole);
	}
	
	
	public static AuthUser projectManager() {
		
		Role managerRole = new Role();
		managerRole.setName("MANAGER");
		managerRole.setPermissions(Permission.PROJECT_MANAGEMENT);

		User managerUser = new User();
		managerUser.setLogin("manager");
		managerUser.setPassword("pass");
		managerUser.setEmail("manager@corolla.com");
		
		return new AuthUser(managerUser, managerRole);
	}
}
