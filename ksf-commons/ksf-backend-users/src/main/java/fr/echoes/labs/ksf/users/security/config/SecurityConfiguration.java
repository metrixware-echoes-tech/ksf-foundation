/**
 *
 */
package fr.echoes.labs.ksf.users.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This class defines the security configuration for Corolla;
 *
 * @author sleroy
 *
 */
@Component
@ConfigurationProperties(prefix = "ksf.security")
public class SecurityConfiguration {
	
	private String internalRememberToken;
	
	private int passwordStrength;
	
	private boolean allowURLRewriting = false;
	
	private int defaultPasswordLength = 8;
	
	private int defaultSaltLength = 32;
	
	public int getDefaultPasswordLength() {
		return defaultPasswordLength;
	}
	
	public int getDefaultSaltLength() {
		return defaultSaltLength;
	}
	
	/**
	 * @return the internalRememberToken
	 */
	public String getInternalRememberToken() {
		return internalRememberToken;
	}
	
	/**
	 * @return the passwordStrength
	 */
	public int getPasswordStrength() {
		return passwordStrength;
	}
	
	/**
	 * @return the allowURLRewriting
	 */
	public boolean isAllowURLRewriting() {
		return allowURLRewriting;
	}
	
	/**
	 * @param _allowURLRewriting
	 *            the allowURLRewriting to set
	 */
	public void setAllowURLRewriting(final boolean _allowURLRewriting) {
		allowURLRewriting = _allowURLRewriting;
	}
	
	public void setDefaultPasswordLength(final int _defaultPasswordLength) {
		defaultPasswordLength = _defaultPasswordLength;
	}
	
	public void setDefaultSaltLength(final int _defaultSaltLength) {
		defaultSaltLength = _defaultSaltLength;
	}
	
	/**
	 * @param _internalRememberToken
	 *            the internalRememberToken to set
	 */
	public void setInternalRememberToken(final String _internalRememberToken) {
		internalRememberToken = _internalRememberToken;
	}
	
	/**
	 * @param _passwordStrength
	 *            the passwordStrength to set
	 */
	public void setPasswordStrength(final int _passwordStrength) {
		passwordStrength = _passwordStrength;
	}
	
	@Override
	public String toString() {
		return "SecurityConfiguration{" + "internalRememberToken=" + internalRememberToken + ", passwordStrength="
				+ passwordStrength + ", allowURLRewriting=" + allowURLRewriting + '}';
	}
	
}
