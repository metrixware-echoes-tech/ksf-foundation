/**
 *
 */
package fr.echoes.labs.ksf.users.security.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

/**
 * This class defines the security configuration for Corolla;
 *
 * @author sleroy
 *
 */
@Component
@ConfigurationProperties(prefix = "ksf.security")
@Order(2)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private String internalRememberToken;

	private int passwordStrength;

	private boolean allowURLRewriting = false;

	private int defaultPasswordLength = 8;

	private int defaultSaltLength = 32;

	public int getDefaultPasswordLength() {
		return this.defaultPasswordLength;
	}

	public int getDefaultSaltLength() {
		return this.defaultSaltLength;
	}

	/**
	 * @return the internalRememberToken
	 */
	public String getInternalRememberToken() {
		return this.internalRememberToken;
	}

	/**
	 * @return the passwordStrength
	 */
	public int getPasswordStrength() {
		return this.passwordStrength;
	}

	/**
	 * @return the allowURLRewriting
	 */
	public boolean isAllowURLRewriting() {
		return this.allowURLRewriting;
	}

	/**
	 * @param _allowURLRewriting
	 *            the allowURLRewriting to set
	 */
	public void setAllowURLRewriting(final boolean _allowURLRewriting) {
		this.allowURLRewriting = _allowURLRewriting;
	}

	public void setDefaultPasswordLength(final int _defaultPasswordLength) {
		this.defaultPasswordLength = _defaultPasswordLength;
	}

	public void setDefaultSaltLength(final int _defaultSaltLength) {
		this.defaultSaltLength = _defaultSaltLength;
	}

	/**
	 * @param _internalRememberToken
	 *            the internalRememberToken to set
	 */
	public void setInternalRememberToken(final String _internalRememberToken) {
		this.internalRememberToken = _internalRememberToken;
	}

	/**
	 * @param _passwordStrength
	 *            the passwordStrength to set
	 */
	public void setPasswordStrength(final int _passwordStrength) {
		this.passwordStrength = _passwordStrength;
	}

	@Override
	public String toString() {
		return "SecurityConfiguration{" + "internalRememberToken=" + this.internalRememberToken + ", passwordStrength="
				+ this.passwordStrength + ", allowURLRewriting=" + this.allowURLRewriting + '}';
	}

	public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
		CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
		casAuthenticationEntryPoint.setLoginUrl("http://localhost:8880/cas/login");
		casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
		return casAuthenticationEntryPoint;
	}

	public ServiceProperties serviceProperties() {
		ServiceProperties sp = new ServiceProperties();
		sp.setService("http://localhost:8888/login/cas");
		sp.setSendRenew(false);
		return sp;
	}


	public SingleSignOutFilter singleSignOutFilter() {
		SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
		singleSignOutFilter.setCasServerUrlPrefix("http://localhost:8880/cas/");
		return singleSignOutFilter;
	}

	public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
		CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
		casAuthenticationFilter.setAuthenticationManager(authenticationManager());
		casAuthenticationFilter.setSessionAuthenticationStrategy(sessionStrategy());
		return casAuthenticationFilter;
	}

	public SessionAuthenticationStrategy sessionStrategy() {
		SessionAuthenticationStrategy sessionStrategy = new SessionFixationProtectionStrategy();
		return sessionStrategy;
	}

	public LogoutFilter requestCasGlobalLogoutFilter() {
		LogoutFilter logoutFilter = new LogoutFilter("http://localhost:8880/cas/logout" + "?service="
				+ "http://localhost:8888/", new SecurityContextLogoutHandler());
		// logoutFilter.setFilterProcessesUrl("/logout");
		// logoutFilter.setFilterProcessesUrl("/j_spring_cas_security_logout");
		logoutFilter.setLogoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"));
		return logoutFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterAfter(new CsrfCookieGeneratorFilter(), CsrfFilter.class).exceptionHandling()
				.authenticationEntryPoint(casAuthenticationEntryPoint()).and().addFilter(casAuthenticationFilter())
				.addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class)
				.addFilterBefore(requestCasGlobalLogoutFilter(), LogoutFilter.class);

		http.headers().frameOptions().disable().authorizeRequests().antMatchers("/").permitAll()
				.antMatchers("/login", "/logout", "/secure").authenticated().antMatchers("/filtered")
				.hasAuthority("ROLE_ADMIN").anyRequest().authenticated();

		/**
		 * <logout invalidate-session="true" delete-cookies="JSESSIONID" />
		 */
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true)
				.deleteCookies("JSESSIONID");

		// http.csrf();
	}

}
