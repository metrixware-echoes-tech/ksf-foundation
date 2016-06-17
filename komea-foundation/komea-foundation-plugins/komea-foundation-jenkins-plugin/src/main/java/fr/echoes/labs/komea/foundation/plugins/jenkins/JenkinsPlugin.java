package fr.echoes.labs.komea.foundation.plugins.jenkins;

import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.komea.foundation.plugins.jenkins.extensions.JenkinsProjectDashboardExtension;
import fr.echoes.labs.komea.foundation.plugins.jenkins.extensions.JenkinsProjectLifeCycleExtension;
import fr.echoes.labs.ksf.plugins.api.PluginPropertiesImpl;
import fr.echoes.labs.pluginfwk.api.extension.IExtension;
import fr.echoes.labs.pluginfwk.api.plugin.Plugin;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginProperties;

@Plugin
public class JenkinsPlugin implements PluginDefinition {

	private final JenkinsProjectLifeCycleExtension	workflowExtension;
	private final JenkinsProjectDashboardExtension	dashboardExtension;

	@Autowired
	public JenkinsPlugin(final JenkinsProjectLifeCycleExtension workflowExtension, final JenkinsProjectDashboardExtension dashboardExtension) {
		super();
		this.workflowExtension = workflowExtension;
		this.dashboardExtension = dashboardExtension;
	}

	@Override
	public void destroy() throws Exception {
		//

	}

	@Override
	public String getDescription() {
		return "Jenkins plugin provides an integration of Foundation with Jenkins repositories";
	}

	@Override
	public IExtension[] getExtensions() {
		return new IExtension[] { this.workflowExtension, this.dashboardExtension };
	}

	@Override
	public String getId() {
		return "Jenkins-plugin";
	}

	@Override
	public String getName() {
		return "Jenkins Plugin";
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
