package fr.echoes.labs.ksf.cc.pluginmanager;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import fr.echoes.labs.pluginfwk.pluginloader.PluginFrameworkConfiguration;

@Component
@ConfigurationProperties(prefix = "ksf.plugins")
public class PluginFrameworkConfigurationBean extends PluginFrameworkConfiguration {

	private File pluginPropertyStorageLocation;

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

}
