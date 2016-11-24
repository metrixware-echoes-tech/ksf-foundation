package fr.echoes.labs.ksf.foreman.api.utils;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Sets;

import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterOverrideValue;
import fr.echoes.labs.ksf.foreman.api.model.SmartVariable;

public final class SmartVariableUtils {

	private SmartVariableUtils() {
		// static class
	}
	
	public static SmartClassParameterOverrideValue getOverrideValueForHost(final SmartVariable variable, final String hostName) {
		return getOverrideValueForMatcher(variable, ForemanEntities.TYPE_FQDN, hostName);
	}
	
	public static SmartClassParameterOverrideValue getOverrideValueForHostGroup(final SmartVariable variable, final String hostGroupName) {
		return getOverrideValueForMatcher(variable, ForemanEntities.TYPE_HOSTGROUP, hostGroupName);
	}
	
	public static SmartClassParameterOverrideValue getOverrideValueForMatcher(final SmartVariable variable, final String matchType, final String matchValue) {
		if (variable != null) {
			return OverrideValueUtils.getOverrideValueForMatcher(variable.getOverrideValues(), matchType, matchValue);
		}		
		return null;
	}
	
	public static Set<String> extractHostGroupNames(final List<SmartVariable> variables) {
		
		return extractByMatcher(variables, ForemanEntities.buildMatcher(ForemanEntities.TYPE_HOSTGROUP, ForemanEntities.ANY_STRING_MATCHER));
	}

	public static Set<String> extractByMatcher(final List<SmartVariable> variables, final String stringMatcher) {
		
		final Pattern pattern = Pattern.compile(stringMatcher);
		final Set<String> results = Sets.newHashSet();
		
		for(final SmartVariable variable : variables) {
			for (final SmartClassParameterOverrideValue value : variable.getOverrideValues()) {
				final Matcher matcher = pattern.matcher(value.getMatch());
				if (matcher.find()) {
					results.add(matcher.group(1));
				}
			}
		}
		
		return results;
	}
	
}
