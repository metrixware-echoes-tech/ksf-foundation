package fr.echoes.labs.pluginfwk.extensions.groovy;

import java.io.File;

public class GroovyPluginConfiguration {

	private File groovyPluginFolder;

	public File getGroovyPluginFolder() {
		return this.groovyPluginFolder;
	}

	public void setGroovyFolder(final File groovyFolder) {
		this.groovyPluginFolder = groovyFolder;
	}
}
