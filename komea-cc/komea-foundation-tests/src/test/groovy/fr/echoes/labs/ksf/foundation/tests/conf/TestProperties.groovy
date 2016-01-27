package fr.echoes.labs.ksf.foundation.tests.conf

import com.pholser.util.properties.BoundProperty;

interface TestProperties {

	@BoundProperty("komea.foundation.url")
	public String serverUrl()
	
	@BoundProperty("foreman.url")
	public String foremanUrl()
	
	@BoundProperty("komea.foundation.defaultUsername")
	public String defaultUsername()
	
	@BoundProperty("komea.foundation.defaultPassword")
	public String defaultPassword()
	
}
