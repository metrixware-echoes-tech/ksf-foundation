package fr.echoes.labs.komea.foundation.plugins.git;

import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.komea.foundation.plugins.git.extensions.GitProjectDashboardExtension;
import fr.echoes.labs.komea.foundation.plugins.git.extensions.GitProjectLifeCycleExtension;
import fr.echoes.labs.ksf.plugins.api.PluginPropertiesImpl;
import fr.echoes.labs.pluginfwk.api.extension.IExtension;
import fr.echoes.labs.pluginfwk.api.plugin.Plugin;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginProperties;

@Plugin
public class GitPlugin implements PluginDefinition {

	private final GitProjectLifeCycleExtension	workflowExtension;
	private final GitProjectDashboardExtension	dashboardExtension;

	@Autowired
	public GitPlugin(final GitProjectLifeCycleExtension workflowExtension, final GitProjectDashboardExtension dashboardExtension) {
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
		return "GIT plugin provides an integration of Foundation with Git repositories";
	}

	@Override
	public IExtension[] getExtensions() {
		return new IExtension[] { this.workflowExtension, this.dashboardExtension };
	}

	@Override
	public String getId() {
		return "git-plugin";
	}

	@Override
	public String getName() {
		return "GIT Plugin";
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
