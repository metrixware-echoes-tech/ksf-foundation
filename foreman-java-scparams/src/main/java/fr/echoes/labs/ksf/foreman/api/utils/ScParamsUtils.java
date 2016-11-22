package fr.echoes.labs.ksf.foreman.api.utils;

import static fr.echoes.labs.ksf.foreman.api.utils.ForemanEntities.ANY_STRING_MATCHER;
import static fr.echoes.labs.ksf.foreman.api.utils.ForemanEntities.TYPE_DOMAIN;
import static fr.echoes.labs.ksf.foreman.api.utils.ForemanEntities.TYPE_FQDN;
import static fr.echoes.labs.ksf.foreman.api.utils.ForemanEntities.TYPE_HOSTGROUP;
import static fr.echoes.labs.ksf.foreman.api.utils.ForemanEntities.TYPE_OS;
import static fr.echoes.labs.ksf.foreman.api.utils.ForemanEntities.buildMatcher;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import fr.echoes.labs.ksf.foreman.api.model.PuppetClass;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameter;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterOverrideValue;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterWrapper;

public final class ScParamsUtils {

	private ScParamsUtils() {
		// static class
	}
	
	public static SmartClassParameterOverrideValue newOverrideValue(final SmartClassParameterWrapper value, final String entityType, final String entityName) {
		
		final SmartClassParameterOverrideValue overrideValue = new SmartClassParameterOverrideValue();
		overrideValue.setMatch(ForemanEntities.buildMatcher(entityType, entityName));

		return mergeOverrideValue(value, overrideValue);
	}
	
	public static SmartClassParameterOverrideValue mergeOverrideValue(final SmartClassParameterWrapper value, final SmartClassParameterOverrideValue overrideValue) {
		
		overrideValue.setValue(value.getValue());
		overrideValue.setUsePuppetDefault(value.getUsePuppetDefault());
		
		return overrideValue;
	}
	
	public static SmartClassParameterOverrideValue getOverrideValueForHost(final SmartClassParameter param, final String host) {
		
		return getOverrideValueForMatcher(param, TYPE_FQDN, host);
	}
	
	public static SmartClassParameterOverrideValue getOverrideValueForHostGroup(final SmartClassParameter param, final String hostGroup) {
		
		SmartClassParameterOverrideValue overrideValue = getOverrideValueForMatcher(param, TYPE_HOSTGROUP, hostGroup);
		
		if (overrideValue == null) {
			overrideValue = getOverrideValueForMatcher(param, TYPE_HOSTGROUP, ForemanEntities.removeParentName(hostGroup));
		}
		
		return overrideValue;
	}
	
	public static SmartClassParameterOverrideValue getOverrideValueForMatcher(final SmartClassParameter param, final String matchType, final String matchValue) {

		if (param != null && param.getOverrideValues() != null) {
			final String matcher = buildMatcher(matchType, matchValue);	
			for (final SmartClassParameterOverrideValue value : param.getOverrideValues()) {
				if (matcher.equals(value.getMatch())) {
					return value;
				}
			}		
		}
		
		return null;
	}
	
	public static Map<String, List<SmartClassParameterWrapper>> groupBy(final String matcherType, final List<SmartClassParameter> params) {
		
		final Map<String, List<SmartClassParameterWrapper>> results = Maps.newHashMap();		
		final Set<String> items = extractByMatcher(params, buildMatcher(matcherType, ANY_STRING_MATCHER));
		
		for (final String item : items) {
			final List<SmartClassParameterWrapper> values = Lists.newArrayList();
			for(final SmartClassParameter param : params) {
				final SmartClassParameterOverrideValue value = getOverrideValueForMatcher(param, matcherType, item);
				if (value != null) {
					values.add(new SmartClassParameterWrapper(param, value));
				}
			}
			results.put(item, values);
		}
		
		return results;
	}
	
	public static Map<String, List<SmartClassParameterWrapper>> groupByOs(final List<SmartClassParameter> params) {
		
		return groupBy(TYPE_OS, params);
	}
	
	public static Map<String, List<SmartClassParameterWrapper>> groupByDomain(final List<SmartClassParameter> params) {
		
		return groupBy(TYPE_DOMAIN, params);
	}
	
	public static Map<String, List<SmartClassParameterWrapper>> groupByHostGroup(final List<SmartClassParameter> params) {
		
		return groupBy(TYPE_HOSTGROUP, params);
	}

	
	public static Set<String> extractHostGroups(final List<SmartClassParameter> params) {
		
		return extractByMatcher(params, buildMatcher(TYPE_HOSTGROUP, ANY_STRING_MATCHER));
	}
	
	public static Set<String> extractOS(final List<SmartClassParameter> params) {
		
		return extractByMatcher(params, buildMatcher(TYPE_OS, ANY_STRING_MATCHER));
	}
	
	public static Set<String> extractDomains(final List<SmartClassParameter> params) {
		
		return extractByMatcher(params, buildMatcher(TYPE_DOMAIN, ANY_STRING_MATCHER));
	}
	
	private static Set<String> extractByMatcher(final List<SmartClassParameter> params, final String stringMatcher) {
		
		final Pattern pattern = Pattern.compile(stringMatcher);
		final Set<String> results = Sets.newHashSet();
		
		for(final SmartClassParameter param : params) {
			for (final SmartClassParameterOverrideValue value : param.getOverrideValues()) {
				final Matcher matcher = pattern.matcher(value.getMatch());
				if (matcher.find()) {
					results.add(matcher.group(1));
				}
			}
		}
		
		return results;
	}
	
	public static SmartClassParameter findByPuppetClassId(final Collection<SmartClassParameter> params, final Integer puppetClassId, final String parameter) {
		
		for (final SmartClassParameter param : params) {		
			if (parameter.equals(param.getParameter())) {
				final PuppetClass puppetClass = param.getPuppetClass();
				if (puppetClass != null && puppetClass.getId().equals(puppetClassId)) {
					return param;
				}
			}		
		}
		
		return null;
	}
	
	public static String toHash(final String value) {
		if (value != null) {
			return value.replace("\\n", "\n").replace("\\r", "\r").replace("\\\"", "\"");
		}
		return null;
	}
	
}
