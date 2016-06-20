package fr.echoes.labs.ksf.cc.pluginmanager;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import fr.echoes.labs.pluginfwk.pluginloader.PluginFrameworkConfiguration;

@Component
@ConfigurationProperties(prefix = "ksf.plugins")
public class PluginFrameworkConfigurationBean extends PluginFrameworkConfiguration {

	public PluginFrameworkConfigurationBean() {
		super();
	}

}
