package fr.echoes.labs.ksf.cc.pluginmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.echoes.labs.pluginfwk.api.extension.ExtensionManager;
import fr.echoes.labs.pluginfwk.api.plugin.PluginFramework;
import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;
import fr.echoes.labs.pluginfwk.pluginloader.PluginFrameworkImpl;

@Configuration
public class PluginManagerFactoryBean {

	@Autowired
	public PluginFrameworkConfigurationBean pluginFrameworkConfiguration;

	@Bean
	public ExtensionManager extensionManager(final PluginFramework _pluginFramework) {
		return _pluginFramework.getExtensionManager();

	}

	@Bean
	public PluginFramework pluginFrameworkImpl() {
		return new PluginFrameworkImpl(this.pluginFrameworkConfiguration);

	}

	@Bean
	public PluginManager pluginManager(final PluginFramework _pluginFramework) {
		return _pluginFramework.getPluginManager();

	}
}
