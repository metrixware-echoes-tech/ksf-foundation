package fr.echoes.labs.ksf.cc.plugins.nexus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.ksf.cc.plugins.nexus.extensions.NexusProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.plugins.nexus.extensions.NexusProjectLifeCycleExtension;
import fr.echoes.labs.ksf.extensions.annotations.Plugin;
import fr.echoes.labs.pluginfwk.api.extension.Extension;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginException;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;

@Plugin
public class NexusPlugin implements PluginDefinition {

	private static final Logger LOGGER = LoggerFactory.getLogger(NexusPlugin.class);
	
	public static final String ID = "nexus";
	
	private final NexusProjectDashboardExtension projectDashboardExtension;
	private final NexusProjectLifeCycleExtension projectLifeCycleExtension;
	
	@Autowired
	public NexusPlugin(final NexusProjectDashboardExtension projectDashboardExtension, final NexusProjectLifeCycleExtension projectLifeCycleExtension) {
		this.projectDashboardExtension = projectDashboardExtension;
		this.projectLifeCycleExtension = projectLifeCycleExtension;
	}
	
	@Override
	public String getDescription() {
		return "Nexus plugin provides an integration of Nexus with Komea Foundation";
	}

	@Override
	public Extension[] getExtensions() {
		return new Extension[] { this.projectDashboardExtension, this.projectLifeCycleExtension };
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public String getName() {
		return "Nexus Plugin";
	}

	@Override
	public Object getPluginProperties() {
		return new NexusConfigurationBean();
	}

	@Override
	public void destroy() throws PluginException {
		// Nothing to do.
	}

	@Override
	public void init(final PluginPropertyStorage propertyStorage) throws PluginException {
		// Nothing to do.
	}

}
