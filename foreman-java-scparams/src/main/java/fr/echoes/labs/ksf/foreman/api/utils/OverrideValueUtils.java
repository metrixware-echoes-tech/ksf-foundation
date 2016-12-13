package fr.echoes.labs.ksf.foreman.api.utils;

import static fr.echoes.labs.ksf.foreman.api.utils.ForemanEntities.buildMatcher;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameter;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterOverrideValue;

public final class OverrideValueUtils {

	private OverrideValueUtils() {
		// static class
	}
	
	public static SmartClassParameterOverrideValue getOverrideValueForMatcher(final List<SmartClassParameterOverrideValue> values, final String matchType, final String matchValue) {

		if (values != null && !values.isEmpty()) {
			final String matcher = buildMatcher(matchType, matchValue);	
			for (final SmartClassParameterOverrideValue value : values) {
				if (matcher.equals(value.getMatch())) {
					return value;
				}
			}		
		}
		
		return null;
	}
	
	public static String toHash(final String value) {
		if (value != null) {
			final String escapedString = value
					.replace("\\r\\n", "\r\n")
					.replace("\\\\n", "\\n")
					.replace("\\\\r", "\\r")
					//.replace("\\n", "\n")
					//.replace("\\r", "\r")
					.replace("\\\\\"", "\\\"")
					.replace("\\\"", "\"")
					.replace("\\\\", "\\");
			return escapedString;
		}
		return null;
	}
	
	public static String formatOverrideValue(final String value, final String type) {
		if (SmartClassParameter.TYPE_HASH.equalsIgnoreCase(type)) {
			// Fix issue with YAML deserialization
			return toHash(value);
		}
		return StringEscapeUtils.unescapeJava(value);
	}
	
}
