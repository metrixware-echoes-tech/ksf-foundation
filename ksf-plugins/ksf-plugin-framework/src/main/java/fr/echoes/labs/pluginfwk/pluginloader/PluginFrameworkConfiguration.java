package fr.echoes.labs.pluginfwk.pluginloader;

import java.io.File;

public class PluginFrameworkConfiguration {

	private File	extraLibsFolder;

	private File	javaPluginFolder		= new File("plugins/java");		//$NON-NLS-1$

	private File	springPluginFolder		= new File("plugins/spring");	//$NON-NLS-1$

	private File	temporaryPluginFolder	= new File("temp/plugins");		//$NON-NLS-1$

	public boolean createTemporaryFolder() {
		return this.temporaryPluginFolder.mkdirs();
	}

	public File getExtraLibsFolder() {
		return this.extraLibsFolder;
	}

	public File getJavaPluginFolder() {
		return this.javaPluginFolder;
	}

	public File getSpringPluginFolder() {
		return this.springPluginFolder;
	}

	public File getTemporaryPluginFolder() {
		return this.temporaryPluginFolder;
	}

	public boolean hasValidTemporaryPluginFolder() {

		return this.temporaryPluginFolder != null && this.temporaryPluginFolder.exists() && this.temporaryPluginFolder.isDirectory();
	}

	public void setExtraLibsFolder(final File extraLibsFolder) {
		this.extraLibsFolder = extraLibsFolder;
	}

	public void setJavaPluginFolder(final File javaPluginFolder) {
		this.javaPluginFolder = javaPluginFolder;
	}

	public void setSpringPluginFolder(final File springPluginFolder) {
		this.springPluginFolder = springPluginFolder;
	}

	public void setTemporaryPluginFolder(final File temporaryPluginFolder) {
		this.temporaryPluginFolder = temporaryPluginFolder;
	}

}
