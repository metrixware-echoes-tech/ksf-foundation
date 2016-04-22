package fr.echoes.labs.ksf.users.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author dcollard
 *
 */
@Component
@ConfigurationProperties(prefix = "ksf.security.cas")
public class CasSecurityConfiguration {

	private String loginUrl;
	private String logoutUrl;
	private String prefix;
	private String serviceUrl;
	private String serviceHome;

	/**
	 * @return the loginUrl
	 */
	public String getLoginUrl() {
		return this.loginUrl;
	}

	/**
	 * @param loginUrl the loginUrl to set
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	/**
	 * @return the logoutUrl
	 */
	public String getLogoutUrl() {
		return this.logoutUrl;
	}

	/**
	 * @param logoutUrl the logoutUrl to set
	 */
	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	/**
	 * @return the prefix
	 */

	public String getPrefix() {
		return this.prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the serviceUrl
	 */
	public String getServiceUrl() {
		return this.serviceUrl;
	}

	/**
	 * @param serviceUrl the serviceUrl to set
	 */
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	/**
	 * @return the serviceHome
	 */
	public String getServiceHome() {
		return this.serviceHome;
	}

	/**
	 * @param serviceHome the serviceHome to set
	 */
	public void setServiceHome(String serviceHome) {
		this.serviceHome = serviceHome;
	}

}
