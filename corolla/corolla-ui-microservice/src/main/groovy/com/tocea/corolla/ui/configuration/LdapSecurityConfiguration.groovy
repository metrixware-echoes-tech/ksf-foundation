/**
 *
 */
package com.tocea.corolla.ui.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

/**
 * This class defines the security configuration for Corolla;
 *
 * @author sleroy
 *
 */
@Component
@ConfigurationProperties(prefix = "corolla.security.ldap")
public class LdapSecurityConfiguration {

	private String	userDnPattern;

	private String	userSearchFilter;

	private String	userSearchBase;

	private String	groupSearchBase;

	private String  groupRoleAttribute;

	private String groupSearchFilter;



	/**
	 * @return the groupRoleAttribute
	 */
	public String getGroupRoleAttribute() {
		return this.groupRoleAttribute;
	}

	/**
	 * @return the groupSearchBase
	 */
	public String getGroupSearchBase() {
		return this.groupSearchBase;
	}

	/**
	 * @return the groupSearchFilter
	 */
	public String getGroupSearchFilter() {
		return this.groupSearchFilter;
	}

	/**
	 * @return the userDnPattern
	 */
	public String getUserDnPattern() {
		return this.userDnPattern;
	}

	public String[] getUserDnPatterns() {
		return Iterables.toArray(Splitter.on(";").omitEmptyStrings().trimResults().split(this.userDnPattern), String.class);
	}

	/**
	 * @return the userSearchBase
	 */
	public String getUserSearchBase() {
		return this.userSearchBase;
	}

	/**
	 * @return the userSearchFilter
	 */
	public String getUserSearchFilter() {
		return this.userSearchFilter;
	}

	public boolean hasUserDN() {

		return !Strings.isNullOrEmpty(this.userDnPattern);
	}

	public boolean hasUserSearch() {

		return !Strings.isNullOrEmpty(this.userSearchFilter);
	}

	/**
	 * @param _groupRoleAttribute the groupRoleAttribute to set
	 */
	public void setGroupRoleAttribute(final String _groupRoleAttribute) {
		this.groupRoleAttribute = _groupRoleAttribute;
	}

	/**
	 * @param _groupSearchBase
	 *            the groupSearchBase to set
	 */
	public void setGroupSearchBase(final String _groupSearchBase) {
		this.groupSearchBase = _groupSearchBase;
	}

	/**
	 * @param _groupSearchFilter the groupSearchFilter to set
	 */
	public void setGroupSearchFilter(final String _groupSearchFilter) {
		this.groupSearchFilter = _groupSearchFilter;
	}

	/**
	 * @param _userDnPattern
	 *            the userDnPattern to set
	 */
	public void setUserDnPattern(final String _userDnPattern) {
		this.userDnPattern = _userDnPattern;
	}

	/**
	 * @param _userSearchBase
	 *            the userSearchBase to set
	 */
	public void setUserSearchBase(final String _userSearchBase) {
		this.userSearchBase = _userSearchBase;
	}

	/**
	 * @param _userSearchFilter
	 *            the userSearchFilter to set
	 */
	public void setUserSearchFilter(final String _userSearchFilter) {
		this.userSearchFilter = _userSearchFilter;
	}
}
