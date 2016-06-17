package fr.echoes.labs.ksf.cc.plugins.foreman;

import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.ksf.cc.plugins.foreman.extensions.ForemanDAOExtension;
import fr.echoes.labs.ksf.cc.plugins.foreman.extensions.ForemanProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.plugins.foreman.extensions.ForemanProjectLifeCycleExtension;
import fr.echoes.labs.ksf.plugins.api.PluginPropertiesImpl;
import fr.echoes.labs.pluginfwk.api.extension.IExtension;
import fr.echoes.labs.pluginfwk.api.plugin.Plugin;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginProperties;

@Plugin
public class ForemanPlugin implements PluginDefinition {

	private final ForemanDAOExtension				foremanDAOExtension;

	private final ForemanProjectDashboardExtension	foremanProjectDashboardExtension;

	private final ForemanProjectLifeCycleExtension	foremanProjectLifeCycleExtension;

	@Autowired
	public ForemanPlugin(final ForemanDAOExtension foremanDAOExtension, final ForemanProjectDashboardExtension foremanProjectDashboardExtension,
			final ForemanProjectLifeCycleExtension foremanProjectLifeCycleExtension) {
		super();
		this.foremanDAOExtension = foremanDAOExtension;
		this.foremanProjectDashboardExtension = foremanProjectDashboardExtension;
		this.foremanProjectLifeCycleExtension = foremanProjectLifeCycleExtension;
	}

	@Override
	public void destroy() {
		//
	}

	@Override
	public String getDescription() {
		return "Foreman plugin for <b>KSF</b>";
	}

	/**
	 * Cette méthode sera utilisée dans la 1.1 , pour définir les extensions
	 * fournies par le plugin. Actuellement dans la 1.0 , les extensions sont
	 * scannées dans le classpath.
	 */
	@Override
	public IExtension[] getExtensions() {

		return new IExtension[] { this.foremanDAOExtension, this.foremanProjectDashboardExtension, this.foremanProjectLifeCycleExtension };
	}

	@Override
	public String getId() {
		return "foreman-plugin";
	}

	@Override
	public String getName() {
		return "Foreman Plugin";
	}

	@Override
	public PluginProperties getPluginProperties() {
		return new PluginPropertiesImpl();
	}

	@Override
	public void init() {

	}

}
