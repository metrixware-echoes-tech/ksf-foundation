package fr.echoes.labs.ksf.cc.plugins.dashboard;

import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.ksf.cc.extensions.gui.ResourceRegistryService;
import fr.echoes.labs.ksf.cc.plugins.dashboard.extensions.DashboardProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.plugins.dashboard.extensions.DashboardProjectLifeCycleExtension;
import fr.echoes.labs.ksf.cc.plugins.dashboard.filters.TimeOnSiteMetricFilter;
import fr.echoes.labs.ksf.extensions.annotations.Plugin;
import fr.echoes.labs.pluginfwk.api.extension.Extension;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;

@Plugin
public class KomeaDashboardPlugin implements PluginDefinition {
	
	public static final String ID = "komea-dashboard";
	public static final String ICON = "/pictures/dashboard.png";

	@Autowired
    private DashboardProjectDashboardExtension dashboardExtension;
	
	@Autowired
    private DashboardProjectLifeCycleExtension dashboardProjectLifeCycleExtension;
    
	@Autowired
	private TimeOnSiteMetricFilter timeOnSiteMetricFilter;

    @Autowired
    private ResourceRegistryService resourceRegistryService;

    @Override
    public void destroy() {
        //
    }

    @Override
    public String getDescription() {
        return "Komea dashboard plugin provides an integration of Komea Dashboard with Komea Foundation";
    }

    @Override
    public Extension[] getExtensions() {
    	return new Extension[] { this.dashboardExtension, this.dashboardProjectLifeCycleExtension };
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Komea Dashboard Plugin";
    }

    @Override
    public Object getPluginProperties() {
        return new DashboardConfigurationBean();
    }

    @Override
    public void init(final PluginPropertyStorage pps) {
    	
    	// Register public resources
    	this.resourceRegistryService.registerResource(this.getClass().getClassLoader(), "pictures", "assets/dashboard.png");
    	
    	// Initializes metrics
        timeOnSiteMetricFilter.initMetrics();
    }

}
