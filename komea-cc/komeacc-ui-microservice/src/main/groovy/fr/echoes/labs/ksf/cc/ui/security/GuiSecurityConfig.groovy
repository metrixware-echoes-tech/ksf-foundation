package fr.echoes.labs.ksf.cc.ui.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

import fr.echoes.lab.ksf.users.security.auth.UserAuthenticationManager

/**
 * Check the ISSUE https://github.com/spring-projects/spring-boot/issues/1801
 * for explanation.
 *
 * @author sleroy
 *
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class GuiSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GuiSecurityConfig.class)
	
	@Autowired
	def UserAuthenticationManager ksfUserAuthentication
	
	@Override
	public void configure(final AuthenticationManagerBuilder auth)
	throws Exception {
		LOGGER.info("Initializing KSF Security")
		ksfUserAuthentication.initializeKsfAuth(auth)
		
	}
	
	// @Override
	@Bean
	public AuthenticationManager getAuthentication() throws Exception {
		return super.authenticationManager()
	}
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		LOGGER.info("Web-- Defining Web Security")
		http.authorizeRequests().antMatchers("/resources/**", "/js/**", "/public/**", "/images/**", "/css/**", "/pictures/**", "/fonts/**", "/login", "/logout", "/", "/favicon.ico").permitAll().
				antMatchers("/ui/**").authenticated().
				antMatchers("/api/**").hasRole("REST")
		
		//anyRequest().permitAll()
		http.formLogin().loginPage("/login").defaultSuccessUrl("/ui/projects").and().logout().logoutUrl("/logout")
		http.csrf().disable()
		
	}
}
