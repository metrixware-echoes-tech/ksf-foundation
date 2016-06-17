package fr.echoes.labs.ksf.extensions.pluginloader;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.echoes.labs.pluginfwk.api.extension.ExtensionManager;
import fr.echoes.labs.pluginfwk.api.extension.ExtensionSearchResultCache;
import fr.echoes.labs.pluginfwk.api.plugin.PluginAlreadyLoadedException;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginInformations;
import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;

@Service
public class PluginManagerImpl implements PluginManager {

	private static final Logger					LOGGER			= LoggerFactory.getLogger(PluginManagerImpl.class.getName() + ".[PLUGINFWK]");

	/** The loaded plugins. */
	private final Map<String, PluginDefinition>	loadedPlugins	= new HashMap<>();

	private final PluginClassLoaderImpl			pluginClassLoader;

	private final ExtensionManager				extensionManager;

	private final ExtensionSearchResultCache	cache;

	@Autowired
	public PluginManagerImpl(final PluginClassLoaderImpl pluginClassLoader, final ExtensionManager extensionManager, final ExtensionSearchResultCache _cache) {
		super();
		this.pluginClassLoader = pluginClassLoader;
		this.extensionManager = extensionManager;
		this.cache = _cache;
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
