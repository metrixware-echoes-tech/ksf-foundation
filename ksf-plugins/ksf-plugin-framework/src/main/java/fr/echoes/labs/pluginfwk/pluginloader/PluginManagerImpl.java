package fr.echoes.labs.pluginfwk.pluginloader;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.echoes.labs.pluginfwk.api.extension.ExtensionManager;
import fr.echoes.labs.pluginfwk.api.plugin.PluginAlreadyLoadedException;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginInformations;
import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;

public class PluginManagerImpl implements PluginManager {

	private static final Logger					LOGGER			= LoggerFactory.getLogger(PluginManagerImpl.class.getName() + ".[PLUGINFWK]");

	/** The loaded plugins. */
	private final Map<String, PluginDefinition>	loadedPlugins	= new HashMap<>();

	private final ExtensionManager				extensionManager;

	private final PluginFrameworkConfiguration	pluginFrameworkConfiguration;

	public PluginManagerImpl(final ExtensionManager extensionManager, final PluginFrameworkConfiguration _pluginFrameworkConfiguration) {
		super();
		this.extensionManager = extensionManager;
		this.pluginFrameworkConfiguration = _pluginFrameworkConfiguration;
	}

	/**
	 * Cleanup.
	 */
	public void cleanup() {
		LOGGER.debug("Closing the loaded plugins");
		this.closingLoadedPlugins();
		this.extensionManager.cleanup();

	}

	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.ksf.extensions.pluginloader.PluginManager#getLoadedPlugins()
	 */
	@Override
	public Collection<PluginDefinition> getLoadedPlugins() {
		return Collections.unmodifiableCollection(this.loadedPlugins.values());
	}

	@Override
	public Collection<PluginInformations> getPluginInformations() {

		return (Collection) this.getLoadedPlugins();
	}

	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.ksf.extensions.pluginloader.PluginManager#registerPlugin(fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition)
	 */
	@Override
	public void registerPlugin(final PluginDefinition pluginDefinition) {
		if (pluginDefinition == null) {
			return;
		}
		LOGGER.info("Registration of the plugin {} with ===> id {}", pluginDefinition.getName(), pluginDefinition.getId());
		this.addPluginToIndex(pluginDefinition);
		this.registerPluginExtensions(pluginDefinition);

	}

	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.ksf.extensions.pluginloader.PluginManager#unregisterPlugin(java.lang.String)
	 */
	@Override
	public void unregisterPlugin(final String pluginID) {
		if (pluginID == null) {
			return;
		}

		LOGGER.info("Unloading the plugin {}", pluginID);
		this.loadedPlugins.remove(pluginID);
		this.extensionManager.removeExtensions(pluginID);
	}

	/**
	 * Adds the plugin to index.
	 *
	 * @param pluginDefinition
	 *            the plugin
	 */
	private void addPluginToIndex(final PluginDefinition pluginDefinition) {
		if (this.loadedPlugins.containsKey(pluginDefinition.getId())) {
			throw new PluginAlreadyLoadedException(pluginDefinition);
		}
		LOGGER.info("Added plugin # {} # to the index", pluginDefinition.getId());
		this.loadedPlugins.put(pluginDefinition.getId(), pluginDefinition);
	}

	private void closingLoadedPlugins() {
		for (final PluginDefinition pluginDefinition : this.loadedPlugins.values()) {
			try {
				LOGGER.info("Closing the plugin {}", pluginDefinition.getName());
				pluginDefinition.destroy();
			} catch (final Exception e) {
				LOGGER.error("Could not liberate the plugin {}", e);
			}
		}

	}

	/**
	 * Register plugin extensions.
	 *
	 * @param pluginDefinition
	 *            the plugin
	 */
	private void registerPluginExtensions(final PluginInformations pluginDefinition) {
		LOGGER.info("Register plugin extensions for # {} #", pluginDefinition.getId());
		this.extensionManager.registerExtensions(pluginDefinition.getId(), pluginDefinition.getExtensions());

	}
}
