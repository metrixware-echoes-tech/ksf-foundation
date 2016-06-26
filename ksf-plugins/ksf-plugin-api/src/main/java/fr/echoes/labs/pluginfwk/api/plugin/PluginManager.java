/*
 *
 */
package fr.echoes.labs.pluginfwk.api.plugin;

import java.util.Collection;

/**
 * The Interface PluginManager defines the component that registers plugins and controls their life cycle.
 */
public interface PluginManager {

	/**
	 * Cleanup the plugin manager.
	 */
	void cleanup();

	/**
	 * Gets the loaded plugins.
	 *
	 * @return the loaded plugins
	 */
	Collection<PluginDefinition> getLoadedPlugins();

	/**
	 * Gets the plugin informations.
	 *
	 * @return the plugin informations
	 */
	Collection<PluginInformations> getPluginInformations();

	/**
	 * Register extension.
	 *
	 * @param pluginDefinition
	 *            the extension
	 */
	void registerPlugin(PluginDefinition pluginDefinition);

	/**
	 * Warn that plugin had a failure (for example at the loading).
	 *
	 * @param failure
	 *            the e
	 */
	void reportPluginFailure(PluginException failure);

	/**
	 * Unregister plugin.
	 *
	 * @param pluginID
	 *            the plugin id
	 * @throws Exception
	 */
	void unregisterPlugin(String pluginID) throws Exception;

}