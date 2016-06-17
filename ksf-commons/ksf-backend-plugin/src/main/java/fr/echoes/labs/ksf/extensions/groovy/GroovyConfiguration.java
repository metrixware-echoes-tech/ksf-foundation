package fr.echoes.labs.ksf.extensions.groovy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration of the CQRS Engine.
 *
 * @author sleroy
 *
 */
@Component
@ConfigurationProperties(prefix = "ksf.extensions.groovy")
public class GroovyConfiguration {

	private String	groovyPluginFolder	= "groovy";

	private boolean	groovyCache			= false;

	public String getGroovyPluginFolder() {
		return this.groovyPluginFolder;
	}

	public boolean isGroovyCache() {
		return this.groovyCache;
	}

	public void setGroovyCache(final boolean groovyCache) {
		this.groovyCache = groovyCache;
	}

	public void setGroovyPluginFolder(final String groovyExtensionFolder) {
		this.groovyPluginFolder = groovyExtensionFolder;
	}

	@Override
	public String toString() {
		return "GroovyConfiguration [groovyExtensionFolder=" + this.groovyPluginFolder + ", groovyCache=" + this.groovyCache + "]";
	}
}
