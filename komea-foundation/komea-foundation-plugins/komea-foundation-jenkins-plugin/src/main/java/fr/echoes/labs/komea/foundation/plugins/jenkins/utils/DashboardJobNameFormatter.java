package fr.echoes.labs.komea.foundation.plugins.jenkins.utils;

import org.apache.commons.lang3.StringUtils;

public class DashboardJobNameFormatter {
	
	private static final String SEPARATOR = " - ";

	public String format(final String jobName, final String parentName) {
		
		StringBuilder result = new StringBuilder();
		
		if (!StringUtils.isEmpty(parentName)) {
			result.append(parentName).append(SEPARATOR);
		}
		
		result.append(jobName);
		
		return result.toString();
	}
	
}
