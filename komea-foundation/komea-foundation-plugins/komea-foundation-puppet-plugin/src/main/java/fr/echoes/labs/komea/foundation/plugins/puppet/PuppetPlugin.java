package fr.echoes.labs.komea.foundation.plugins.puppet;

import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.komea.foundation.plugins.puppet.extensions.PuppetProjectDashboardExtension;
import fr.echoes.labs.ksf.plugins.api.PluginPropertiesImpl;
import fr.echoes.labs.pluginfwk.api.extension.IExtension;
import fr.echoes.labs.pluginfwk.api.plugin.Plugin;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginProperties;

@Plugin
public class PuppetPlugin implements PluginDefinition {

	private final PuppetProjectDashboardExtension dashboardExtension;

	@Autowired
	public PuppetPlugin(final PuppetProjectDashboardExtension dashboardExtension) {
		super();
		this.dashboardExtension = dashboardExtension;
	}

	@Override
	public void destroy() throws Exception {
		//

	}

	@Override
	public String getDescription() {
		return "Puppet plugin provides an integration of Foundation with Puppet repositories";
	}

	@Override
	public IExtension[] getExtensions() {
		return new IExtension[] { this.dashboardExtension };
	}

	@Override
	public String getId() {
		return "Puppet-plugin";
	}

	@Override
	public String getName() {
		return "Puppet Plugin";
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
