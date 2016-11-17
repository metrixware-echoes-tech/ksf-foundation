package fr.echoes.labs.komea.foundation.plugins.git;

import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.komea.foundation.plugins.git.extensions.GitProjectDashboardExtension;
import fr.echoes.labs.komea.foundation.plugins.git.extensions.GitProjectLifeCycleExtension;
import fr.echoes.labs.ksf.extensions.annotations.Plugin;
import fr.echoes.labs.pluginfwk.api.extension.Extension;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginException;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;

@Plugin
public class GitPlugin implements PluginDefinition {

	public static final String ID = "git";
	public static final String MERGE_ERROR = "gitMergeError";
	
	private final GitProjectDashboardExtension projectDashboardExtension;
	private final GitProjectLifeCycleExtension projectLifeCycleExtension;
	
	@Autowired
	public GitPlugin(final GitProjectDashboardExtension projectDashboardExtension, final GitProjectLifeCycleExtension projectLifeCycleExtension) {
		this.projectDashboardExtension = projectDashboardExtension;
		this.projectLifeCycleExtension = projectLifeCycleExtension;
	}
	
	@Override
	public String getDescription() {
		return "Git plugin provides an integration of Git with Komea Foundation";
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
		return "Git Plugin";
	}

	@Override
	public Object getPluginProperties() {
		return new GitConfigurationBean();
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
