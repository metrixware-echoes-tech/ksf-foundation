package fr.echoes.labs.ksf.cc.ui.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

import fr.echoes.labs.ksf.users.security.auth.IUserAuthenticationManager

/**
 * Check the ISSUE https://github.com/spring-projects/spring-boot/issues/1801
 * for explanation.
 *
 * @author sleroy
 *
 */
@Profile("casAuth")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class CasGuiSecurityConfig extends CasWebSecurityConfigurerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CasGuiSecurityConfig.class)

	@Autowired
	def IUserAuthenticationManager ksfUserAuthentication

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

}
