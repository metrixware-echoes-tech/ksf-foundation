package fr.echoes.labs.ksf.plugins.utils;

import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;

public final class ConfigurationPropertiesUtils {
	
	private static final String VARIABLE_PREFIX = "%{";

	private ConfigurationPropertiesUtils() {
		// static class
	}
	
	public static String replaceVariables(final String str, final Map<String, String> variables) {
        final StrSubstitutor sub = new StrSubstitutor(variables);
        sub.setVariablePrefix(VARIABLE_PREFIX);
        return sub.replace(str);
    }
	
}
