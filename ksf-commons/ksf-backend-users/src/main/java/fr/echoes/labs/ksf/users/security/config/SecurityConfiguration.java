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

}
