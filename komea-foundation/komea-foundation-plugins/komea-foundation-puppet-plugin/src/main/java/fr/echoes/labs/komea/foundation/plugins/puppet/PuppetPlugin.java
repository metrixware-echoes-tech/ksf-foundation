package fr.echoes.labs.komea.foundation.plugins.puppet;

import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.komea.foundation.plugins.puppet.extensions.PuppetProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.extensions.gui.ResourceRegistryService;
import fr.echoes.labs.ksf.extensions.annotations.Plugin;
import fr.echoes.labs.pluginfwk.api.extension.Extension;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginException;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;

@Plugin
public class PuppetPlugin implements PluginDefinition {

	public static final String ID = "puppet";
	public static final String ICON = "/pictures/puppet.png";
	
	@Autowired
	private PuppetProjectDashboardExtension projectDashboardExtension;
	
	@Autowired
	private ResourceRegistryService resourceRegistryService;
	
	@Override
	public String getDescription() {
		return "Puppet plugin provides an integration of Puppet with Komea Foundation";
	}

	@Override
	public Extension[] getExtensions() {
		return new Extension[] { this.projectDashboardExtension };
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public String getName() {
		return "Puppet Plugin";
	}

	@Override
	public Object getPluginProperties() {
		return null;
	}

	@Override
	public void destroy() throws PluginException {
		// Nothing to do.
	}

	@Override
	public void init(final PluginPropertyStorage propertyStorage) throws PluginException {
		// Register public resources
		this.resourceRegistryService.registerResource(this.getClass().getClassLoader(), "pictures", "assets/puppet.png");
	}
	
}
