package fr.echoes.labs.ksf.cc.plugins;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import fr.echoes.labs.pluginfwk.api.plugin.PluginFrameworkConfiguration;

@Component
@ConfigurationProperties(prefix = "ksf.plugins")
public class PluginFrameworkConfigurationBean implements PluginFrameworkConfiguration {

	private File pluginPropertyStorageLocation;
	private File extraLibsFolder;
	private File temporaryPluginFolder;
	private File javaPluginFolder;
	private File springPluginFolder;

	public PluginFrameworkConfigurationBean() {
		super();
	}

	public void createPluginConfigurationStorageFolder() {
		this.pluginPropertyStorageLocation.mkdirs();
	}

	public File getPluginPropertyStorageLocation() {
		return this.pluginPropertyStorageLocation;
	}

	public void setPluginPropertyStorageLocation(final File pluginPropertyStorageLocation) {
		this.pluginPropertyStorageLocation = pluginPropertyStorageLocation;
	}

	@Override
	public boolean createTemporaryFolder() {
		return true;
	}

	@Override
	public File getExtraLibsFolder() {
		return this.extraLibsFolder;
	}

	@Override
	public File getTemporaryPluginFolder() {
		return this.temporaryPluginFolder;
	}

	@Override
	public boolean hasValidTemporaryPluginFolder() {
		return true;
	}
	
	public void setJavaPluginFolder(final File javaPluginFolder) {
		this.javaPluginFolder = javaPluginFolder;
	}

	public File getJavaPluginFolder() {
		return javaPluginFolder;
	}
	
	public void setExtraLibsFolder(File extraLibsFolder) {
		this.extraLibsFolder = extraLibsFolder;
	}
	
	public void setTemporaryPluginFolder(File temporaryPluginFolder) {
		this.temporaryPluginFolder = temporaryPluginFolder;
	}

	public File getSpringPluginFolder() {
		return springPluginFolder;
	}

	public void setSpringPluginFolder(File springPluginFolder) {
		this.springPluginFolder = springPluginFolder;
	}
	
}
