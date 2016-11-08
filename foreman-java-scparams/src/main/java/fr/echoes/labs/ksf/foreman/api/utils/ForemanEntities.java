package fr.echoes.labs.ksf.foreman.api.utils;

public final class ForemanEntities {

	public static final String TYPE_FQDN = "fqdn";
	public static final String TYPE_HOSTGROUP = "hostgroup";
	public static final String TYPE_DOMAIN = "domain";
	public static final String TYPE_OS = "os";
	public static final String ANY_STRING_MATCHER = "(.*)";
	
	private ForemanEntities() {
		// static class
	}
	
	public static String buildMatcher(final String type, final String value) {
		return type+"="+value;
	}
	
	public static String hostGroupMatcher(final String hostGroup) {
		return buildMatcher(TYPE_HOSTGROUP, hostGroup);
	}
	
	public static String hostMatcher(final String hostName) {
		return buildMatcher(TYPE_FQDN, hostName);
	}
	
	public static String removeParentName(final String hostGroupName) {
		
		if (hostGroupName.contains("/")) {
			final String[] parts = hostGroupName.split("/");
			return parts[parts.length-1];
		}
		
		return hostGroupName;
	}
	
}
