package fr.echoes.labs.pluginfwk.api.plugin;

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
	 */

	void init();
}
