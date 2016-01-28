package fr.echoes.labs.ksf.foundation.tests.conf

import com.pholser.util.properties.BoundProperty;

interface TestProperties {
	
	/** Web Element Selectors */
	public static final String PROJECT_LIST_SELECTOR = "project-list"
	
	/**
	 * Time to wait before sending a Time Out Exception
	 * (in seconds)
	 */
	public static final long DEFAULT_LOOKUP_TIMEOUT = 30;
	
	@BoundProperty("komea.foundation.url")
	public String serverUrl()
	
	@BoundProperty("komea.foundation.defaultUsername")
	public String defaultUsername()
	
	@BoundProperty("komea.foundation.defaultPassword")
	public String defaultPassword()
	
	@BoundProperty("foreman.url")
	public String foremanUrl()

}
