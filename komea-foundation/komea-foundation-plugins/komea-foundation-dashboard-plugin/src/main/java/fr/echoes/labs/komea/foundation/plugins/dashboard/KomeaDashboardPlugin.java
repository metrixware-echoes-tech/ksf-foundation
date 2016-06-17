package fr.echoes.labs.komea.foundation.plugins.dashboard;

import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.komea.foundation.plugins.dashboard.extensions.DashboardProjectDashboardExtension;
import fr.echoes.labs.komea.foundation.plugins.dashboard.extensions.DashboardProjectLifeCycleExtension;
import fr.echoes.labs.ksf.plugins.api.PluginPropertiesImpl;
import fr.echoes.labs.pluginfwk.api.extension.IExtension;
import fr.echoes.labs.pluginfwk.api.plugin.Plugin;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginProperties;

@Plugin
public class KomeaDashboardPlugin implements PluginDefinition {

	private final DashboardProjectDashboardExtension	dashboardExtension;
	private final DashboardProjectLifeCycleExtension	dashboardProjectLifeCycleExtension;

	@Autowired
	public KomeaDashboardPlugin(final DashboardProjectDashboardExtension dashboardExtension,
			final DashboardProjectLifeCycleExtension dashboardProjectLifeCycleExtension) {
		super();
		this.dashboardExtension = dashboardExtension;
		this.dashboardProjectLifeCycleExtension = dashboardProjectLifeCycleExtension;
	}

	@Override
	public void destroy() throws Exception {
		//

	}

	@Override
	public String getDescription() {
		return "Komea dashboard plugin provides an integration of Komea Dashboard with Komea Foundation";
	}

	@Override
	public IExtension[] getExtensions() {
		return new IExtension[] { this.dashboardExtension, this.dashboardProjectLifeCycleExtension };
	}

	@Override
	public String getId() {
		return "komea-dashboard";
	}

	@Override
	public String getName() {
		return "Komea Dashboard Integration";
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
