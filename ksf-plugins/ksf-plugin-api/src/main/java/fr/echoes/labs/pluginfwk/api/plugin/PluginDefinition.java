package fr.echoes.labs.pluginfwk.api.plugin;

import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;

/**
 * The interface FoundationPlugin defines the methods to be implemented by a plugin.
 */
public interface PluginDefinition extends PluginInformations {

	/**
	 * Destroy.
	 */
	void destroy() throws Exception;

	/**
	 * Initializes the plugin.
	 *
	 * @param _pluginPropertyStorage
	 *            the plugin property storage
	 */

	void init(PluginPropertyStorage _pluginPropertyStorage);
}
