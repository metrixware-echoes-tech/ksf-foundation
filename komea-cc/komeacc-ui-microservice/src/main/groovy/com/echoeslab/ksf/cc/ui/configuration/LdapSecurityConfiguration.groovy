/**
 *
 */
package com.echoeslab.ksf.cc.ui.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import groovy.transform.Canonical
import groovy.transform.Canonical

/**
 * This class defines the security configuration for Corolla;
 *
 * @author sleroy
 *
 */
@Component
@ConfigurationProperties(prefix = "corolla.security.ldap")
@Canonical
public class LdapSecurityConfiguration {

	def String	userDnPattern;

	def String	userSearchFilter;

	def String	userSearchBase;

	def String	groupSearchBase;

	def String  groupRoleAttribute;

	def String groupSearchFilter;


}
