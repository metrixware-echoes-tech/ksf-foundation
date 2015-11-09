/**
 *
 */
package fr.echoes.lab.ksf.users.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.stereotype.Component;

import fr.echoes.lab.ksf.users.security.utils.SecurityLoggers;

/**
 * This class defines the security configuration for Corolla;
 *
 * @author sleroy
 *
 */
@Component
@ConfigurationProperties(prefix = "ksf.security.ldap")
public class LdapSecurityConfiguration {

	private String url;

	private String	field_cn		= "cn";
	private String	managerDn	= "";

	private String managerPassword = "";

	private String root = "";

	private Integer port = 389;

	private String userDnPattern = "";

	private String userSearchFilter = "uid={0},ou=people";

	private String userDetailsLookup = "cn={0},ou=people";

	private String userSearchBase = "";

	private String groupSearchBase = "ou=groups";

	private String groupRoleAttribute = "";

	private String groupSearchFilter = "";

	private boolean pooled = false;

	public String field_sn = "sn";

	public String field_uid = "uid";

	public String field_givenname = "givenname";

	public String field_mail = "mail";

	/**
	 * LDAP Context contains the LDAP Url and the LDAP Authentication
	 *
	 * @return the spring ldap configuration.
	 */
	public DefaultSpringSecurityContextSource buildLdapContext() {
		final DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(
				getProviderUrl());
		SecurityLoggers.LDAP_LOGGER.info("LDAP Connection informations : {}", this);
		if (!managerDn.isEmpty()) {
			SecurityLoggers.LDAP_LOGGER.info("LDAP Connection requires authentication");
			contextSource.setUserDn(managerDn);
			if (managerPassword == null) {
				throw new IllegalStateException("managerPassword is required if managerDn is supplied");
			}
			contextSource.setPassword(managerPassword);
		} else {
			SecurityLoggers.LDAP_LOGGER.info("LDAP Authentication use anonymous read-only mode");
			contextSource.setAnonymousReadOnly(true);
		}
		contextSource.afterPropertiesSet(); // TODO::Required to initialize the
		// conf, could be converted into a
		// bean later.. with @Component or
		// @Bean
		return contextSource;
	}

	/**
	 * Le ManagerDN doit être à null pour activer l'accès anonymous.
	 */
	public void clearManagerDN() {
		if (managerDn == "") {
			managerDn = null;
		}
	}

	public String getField_cn() {
		return field_cn;
	}

	/**
	 * @return the field_givenname
	 */
	public String getField_givenname() {
		return field_givenname;
	}

	/**
	 * @return the field_mail
	 */
	public String getField_mail() {
		return field_mail;
	}

	/**
	 * @return the field_sn
	 */
	public String getField_sn() {
		return field_sn;
	}

	/**
	 * @return the field_uid
	 */
	public String getField_uid() {
		return field_uid;
	}

	public String getGroupRoleAttribute() {
		return groupRoleAttribute;
	}

	public String getGroupSearchBase() {
		return groupSearchBase;
	}

	public String getGroupSearchFilter() {
		return groupSearchFilter;
	}

	public String getManagerDn() {
		return managerDn;
	}

	public String getManagerPassword() {
		return managerPassword;
	}

	public Integer getPort() {
		return port;
	}

	public String getProviderUrl() {
		if (url == null) {
			return "<undefined>";
		}
		return url + ":" + port + "/" + root;

	}

	public String getRoot() {
		return root;
	}

	public String getUrl() {
		return url;
	}

	public String getUserDetailsLookup() {
		return userDetailsLookup;
	}

	public String getUserDnPattern() {
		return userDnPattern;
	}

	public String getUserSearchBase() {
		return userSearchBase;
	}

	public String getUserSearchFilter() {
		return userSearchFilter;
	}

	/**
	 * Vérifie si une URL est fournie pour LDAP. Si pas d'URL, pas
	 * d'authentification.
	 */
	public boolean hasConfigurationProvided() {
		return url != null && !url.isEmpty();
	}

	public boolean hasUserDN() {
		return !userDnPattern.isEmpty();
	}

	public boolean hasUserSearch() {
		return !userSearchBase.isEmpty();
	}

	/**
	 * Vérifie si l'authentification est requise dans l'application
	 * (user,password)
	 */
	public boolean isAuthRequired() {
		return managerDn != null;
	}

	public boolean isPooled() {
		return pooled;
	}

	public void setField_cn(final String _field_cn) {
		field_cn = _field_cn;
	}

	/**
	 * @param _field_givenname
	 *            the field_givenname to set
	 */
	public void setField_givenname(final String _field_givenname) {
		field_givenname = _field_givenname;
	}

	/**
	 * @param _field_mail
	 *            the field_mail to set
	 */
	public void setField_mail(final String _field_mail) {
		field_mail = _field_mail;
	}

	/**
	 * @param _field_sn
	 *            the field_sn to set
	 */
	public void setField_sn(final String _field_sn) {
		field_sn = _field_sn;
	}

	/**
	 * @param _field_uid
	 *            the field_uid to set
	 */
	public void setField_uid(final String _field_uid) {
		field_uid = _field_uid;
	}

	public void setGroupRoleAttribute(final String groupRoleAttribute) {
		this.groupRoleAttribute = groupRoleAttribute;
	}

	public void setGroupSearchBase(final String groupSearchBase) {
		this.groupSearchBase = groupSearchBase;
	}

	public void setGroupSearchFilter(final String groupSearchFilter) {
		this.groupSearchFilter = groupSearchFilter;
	}

	public void setManagerDn(final String managerDn) {
		this.managerDn = managerDn;
	}

	public void setManagerPassword(final String managerPassword) {
		this.managerPassword = managerPassword;
	}

	public void setPooled(final boolean pooled) {
		this.pooled = pooled;
	}

	public void setPort(final Integer port) {
		this.port = port;
	}

	public void setRoot(final String root) {
		this.root = root;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public void setUserDetailsLookup(final String _userDetailsLookup) {
		userDetailsLookup = _userDetailsLookup;
	}

	public void setUserDnPattern(final String userDnPattern) {
		this.userDnPattern = userDnPattern;
	}

	public void setUserSearchBase(final String userSearchBase) {
		this.userSearchBase = userSearchBase;
	}
	
	public void setUserSearchFilter(final String userSearchFilter) {
		this.userSearchFilter = userSearchFilter;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LdapSecurityConfiguration [url=" + url + ", field_cn=" + field_cn + ", managerDn=" + managerDn
				+ ", managerPassword=" + managerPassword + ", root=" + root + ", port=" + port + ", userDnPattern="
				+ userDnPattern + ", userSearchFilter=" + userSearchFilter + ", userSearchBase=" + userSearchBase
				+ ", groupSearchBase=" + groupSearchBase + ", groupRoleAttribute=" + groupRoleAttribute
				+ ", groupSearchFilter=" + groupSearchFilter + ", pooled=" + pooled + ", field_sn=" + field_sn
				+ ", field_uid=" + field_uid + ", field_givenname=" + field_givenname + ", field_mail=" + field_mail
				+ "]";
	}
}
