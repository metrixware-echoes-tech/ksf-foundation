package fr.echoes.labs.pluginfwk.pluginloader;

import java.io.File;

public class PluginFrameworkConfiguration {

	private File extraLibsFolder;

	public File getExtraLibsFolder() {
		return this.extraLibsFolder;
	}

	public void setExtraLibsFolder(final File extraLibsFolder) {
		this.extraLibsFolder = extraLibsFolder;
	}

}
