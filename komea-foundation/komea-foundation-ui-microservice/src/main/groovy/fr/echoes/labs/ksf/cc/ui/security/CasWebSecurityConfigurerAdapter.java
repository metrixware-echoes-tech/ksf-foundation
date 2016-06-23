package fr.echoes.labs.ksf.cc.ui.security;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import fr.echoes.labs.ksf.users.security.config.CasSecurityConfiguration;

/**
 * @author dcollard
 *
 */
public class CasWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

	@Autowired
	private CasSecurityConfiguration conf;

	public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
		final CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
		casAuthenticationEntryPoint.setLoginUrl(this.conf.getLoginUrl());
		casAuthenticationEntryPoint.setServiceProperties(this.serviceProperties());
		return casAuthenticationEntryPoint;
	}

	public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
		final CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
		casAuthenticationFilter.setAuthenticationManager(this.authenticationManager());
		casAuthenticationFilter.setSessionAuthenticationStrategy(this.sessionStrategy());
		return casAuthenticationFilter;
	}

	public LogoutFilter requestCasGlobalLogoutFilter() {
		final LogoutFilter logoutFilter = new LogoutFilter(this.conf.getLogoutUrl() + "?service=" + this.conf.getServiceHome(),
				new SecurityContextLogoutHandler());
		logoutFilter.setLogoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"));
		return logoutFilter;
	}

	public ServiceProperties serviceProperties() {
		final ServiceProperties sp = new ServiceProperties();
		sp.setService(this.conf.getServiceUrl());
		sp.setSendRenew(false);
		return sp;
	}

	public SessionAuthenticationStrategy sessionStrategy() {
		final SessionAuthenticationStrategy sessionStrategy = new SessionFixationProtectionStrategy();
		return sessionStrategy;
	}

	public SingleSignOutFilter singleSignOutFilter() {
		final SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
		singleSignOutFilter.setCasServerUrlPrefix(this.conf.getPrefix());
		return singleSignOutFilter;
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		// http.addFilterAfter(new CsrfCookieGeneratorFilter(), CsrfFilter.class).exceptionHandling()
		// .authenticationEntryPoint(casAuthenticationEntryPoint()).and().addFilter(casAuthenticationFilter())
		// .addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class)
		// .addFilterBefore(requestCasGlobalLogoutFilter(), LogoutFilter.class);

		http.exceptionHandling().authenticationEntryPoint(this.casAuthenticationEntryPoint()).and().addFilter(this.casAuthenticationFilter()).addFilterBefore(
				this.singleSignOutFilter(), CasAuthenticationFilter.class).addFilterBefore(this.requestCasGlobalLogoutFilter(), LogoutFilter.class);

		http.headers().frameOptions().disable().and().authorizeRequests().antMatchers("/").permitAll().antMatchers("/login", "/logout",
				"/secure").authenticated().antMatchers("/filtered").hasAuthority("ROLE_ADMIN").anyRequest().authenticated();

		// <logout invalidate-session="true" delete-cookies="JSESSIONID" />
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID");
		http.csrf().disable();
	}
}
