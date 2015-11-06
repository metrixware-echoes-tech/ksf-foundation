package com.tocea.corolla.ui.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.core.annotation.Order
import com.tocea.corolla.ui.configuration.LdapSecurityConfiguration
import com.tocea.corolla.ui.configuration.SecurityConfiguration


/**
 * Check the ISSUE https://github.com/spring-projects/spring-boot/issues/1801 for explanation.
 * @author sleroy
 *
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class GuiSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GuiSecurityConfig.class)
	
	@Autowired
	private UserDetailsService			userDetailsService
	
	@Autowired
	private SecurityConfiguration		security
	
	@Autowired
	private LdapSecurityConfiguration	ldapSecurity
	
	@Autowired
	private PasswordEncoder				passwordEncoder
	
	@Override
	public void configure(final AuthenticationManagerBuilder auth)
	throws Exception {
		// LOGGER.info("SECURITY : Configuration of Authentication");
		// /** LDAP Security */
		if (this.ldapSecurity.hasUserDN()) {
			auth.ldapAuthentication()
					.userDnPatterns(this.ldapSecurity.getUserDnPatterns())
					.groupSearchBase(this.ldapSecurity.getGroupSearchBase())
					.groupRoleAttribute(this.ldapSecurity.getGroupRoleAttribute())
					.groupSearchFilter(this.ldapSecurity.getGroupSearchFilter())
		} else if (this.ldapSecurity.hasUserSearch()) {
			auth.ldapAuthentication()
					.userSearchBase(this.ldapSecurity.getUserSearchBase())
					.userSearchFilter(this.ldapSecurity.getUserSearchFilter())
					.groupSearchBase(this.ldapSecurity.getGroupSearchBase())
					.groupRoleAttribute(this.ldapSecurity.getGroupRoleAttribute())
					.groupSearchFilter(this.ldapSecurity.getGroupSearchFilter())
		}
		final DaoAuthenticationProvider authenticationProvider = new
				DaoAuthenticationProvider()
		authenticationProvider.setPasswordEncoder(new
				BCryptPasswordEncoder())
		authenticationProvider.setUserDetailsService(this.userDetailsService)
		auth.authenticationProvider(authenticationProvider)
		
		//		auth.inMemoryAuthentication().withUser("user").password("password1")
		//				.roles("GUI")
	}
	
	// @Override
	@Bean
	public AuthenticationManager getAuthentication() throws Exception {
		return super.authenticationManager()
	}
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		LOGGER.info("Web-- Defining Web Security")
		http.authorizeRequests().antMatchers("/resources/**", "/js/**", "/public/**", "/images/**", "/css/**", "/pictures/**", "/fonts/**", "/login", "/logout","/", "/favicon.ico").permitAll().
				antMatchers("/ui/**").authenticated().
				antMatchers("/api/**").hasRole("REST")
		
		//anyRequest().permitAll()
		http.formLogin().loginPage("/login").defaultSuccessUrl("/home").and().logout().logoutUrl("/logout")
		http.csrf().disable()
		
		
	}
}