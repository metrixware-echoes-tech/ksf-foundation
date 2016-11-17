package fr.echoes.labs.ksf.cc.plugins.foreman;

import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.ksf.cc.plugins.foreman.extensions.ForemanProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.plugins.foreman.extensions.ForemanProjectLifeCycleExtension;
import fr.echoes.labs.ksf.extensions.annotations.Plugin;
import fr.echoes.labs.pluginfwk.api.extension.Extension;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginException;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;

@Plugin
public class ForemanPlugin implements PluginDefinition {
	
	public static final String ID = "foreman";
	
	@Autowired
	private ForemanProjectDashboardExtension projectDashboardExtension;
	
	@Autowired
	private ForemanProjectLifeCycleExtension projectLifeCycleExtension;

	@Override
	public String getDescription() {
		return "Foreman plugin provides an integration of Foreman with Komea Foundation";
	}
	
	@Override
	public String getId() {
		return ID;
	}

	@Override
	public Extension[] getExtensions() {
		return new Extension[] { this.projectDashboardExtension, this.projectLifeCycleExtension };
	}

	@Override
	public String getName() {
		return "Foreman Plugin";
	}

	@Override
	public void destroy() throws PluginException {
		// Nothing to do.
	}

	@Override
	public void init(final PluginPropertyStorage propertyStorage) throws PluginException {
		// Nothing to do.
	}

	@Override
	public Object getPluginProperties() {
		return new ForemanConfigurationBean();
	}
	
}
