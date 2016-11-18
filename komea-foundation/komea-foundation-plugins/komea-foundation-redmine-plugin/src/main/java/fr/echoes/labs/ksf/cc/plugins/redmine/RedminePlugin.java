package fr.echoes.labs.ksf.cc.plugins.redmine;

import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.ksf.cc.plugins.redmine.extensions.RedmineProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.plugins.redmine.extensions.RedmineProjectLifeCycleExtension;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.RedmineProjectFreature;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.RedmineProjectRelease;
import fr.echoes.labs.ksf.extensions.annotations.Plugin;
import fr.echoes.labs.pluginfwk.api.extension.Extension;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginException;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;

@Plugin
public class RedminePlugin implements PluginDefinition {

	public static final String ID = "redmine";
	
	@Autowired
	private RedmineProjectDashboardExtension projectDashboardExtension;
	
	@Autowired
	private RedmineProjectLifeCycleExtension projectLifeCycleExtension;
	
	@Autowired
	private RedmineProjectFreature projectFeaturesExtension;
	
	@Autowired
	private RedmineProjectRelease projectReleasesExtension;
	
	@Override
	public String getDescription() {
		return "Redmine plugin provides an integration of Redmine with Komea Foundation";
	}

	@Override
	public Extension[] getExtensions() {
		return new Extension[] { 
				this.projectDashboardExtension, 
				this.projectLifeCycleExtension,
				this.projectReleasesExtension,
				this.projectFeaturesExtension
		};
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public String getName() {
		return "Redmine Plugin";
	}

	@Override
	public Object getPluginProperties() {
		return new RedmineConfigurationBean();
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
