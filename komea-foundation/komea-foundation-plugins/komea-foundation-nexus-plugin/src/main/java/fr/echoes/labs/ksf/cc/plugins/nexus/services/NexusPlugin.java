package fr.echoes.labs.ksf.cc.plugins.nexus.services;

import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.komea.foundation.plugins.nexus.extensions.NexusProjectDashboardExtension;
import fr.echoes.labs.ksf.plugins.api.PluginPropertiesImpl;
import fr.echoes.labs.pluginfwk.api.extension.IExtension;
import fr.echoes.labs.pluginfwk.api.plugin.Plugin;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginProperties;

@Plugin
public class NexusPlugin implements PluginDefinition {

	private final NexusProjectDashboardExtension dashboardExtension;

	@Autowired
	public NexusPlugin(final NexusProjectDashboardExtension dashboardExtension) {
		super();
		this.dashboardExtension = dashboardExtension;
	}

	@Override
	public void destroy() throws Exception {
		//

	}

	@Override
	public String getDescription() {
		return "Nexus plugin provides an integration of Foundation with Nexus repositories";
	}

	@Override
	public IExtension[] getExtensions() {
		return new IExtension[] { this.dashboardExtension };
	}

	@Override
	public String getId() {
		return "Nexus-plugin";
	}

	@Override
	public String getName() {
		return "Nexus Plugin";
	}

	@Override
	public PluginProperties getPluginProperties() {
		return new PluginPropertiesImpl();
	}

	@Override
	public void init() {
		//

	}

}
