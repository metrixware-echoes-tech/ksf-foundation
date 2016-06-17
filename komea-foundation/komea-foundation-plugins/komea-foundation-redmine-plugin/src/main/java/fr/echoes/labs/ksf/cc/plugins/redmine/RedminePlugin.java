package fr.echoes.labs.ksf.cc.plugins.redmine;

import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.ksf.cc.plugins.redmine.extensions.RedmineProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.plugins.redmine.extensions.RedmineProjectLifeCycleExtension;
import fr.echoes.labs.ksf.plugins.api.PluginPropertiesImpl;
import fr.echoes.labs.pluginfwk.api.extension.IExtension;
import fr.echoes.labs.pluginfwk.api.plugin.Plugin;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginProperties;

@Plugin
public class RedminePlugin implements PluginDefinition {

	private final RedmineProjectDashboardExtension	redmineExtension;
	private final RedmineProjectLifeCycleExtension	redmineProjectLifeCycleExtension;

	@Autowired
	public RedminePlugin(final RedmineProjectDashboardExtension RedmineExtension, final RedmineProjectLifeCycleExtension RedmineProjectLifeCycleExtension) {
		super();
		this.redmineExtension = RedmineExtension;
		this.redmineProjectLifeCycleExtension = RedmineProjectLifeCycleExtension;
	}

	@Override
	public void destroy() throws Exception {
		//

	}

	@Override
	public String getDescription() {
		return "Redmine plugin provides an integration of Redmine with Komea Foundation";
	}

	@Override
	public IExtension[] getExtensions() {
		return new IExtension[] { this.redmineExtension, this.redmineProjectLifeCycleExtension };
	}

	@Override
	public String getId() {
		return "redmine-plugin";
	}

	@Override
	public String getName() {
		return "Redmine Integration";
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
