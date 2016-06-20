package fr.echoes.labs.pluginfwk.api.plugin;

import java.io.Closeable;

import fr.echoes.labs.pluginfwk.api.extension.ExtensionManager;

public interface PluginFramework extends Closeable {

	/**
	 * Declare plugin scanner.
	 *
	 * @param _pluginScanner
	 *            the plugin scanner
	 */
	void declarePluginScanner(PluginScanner _pluginScanner);

	/**
	 * Gets the extension manager.
	 *
	 * @return the extension manager
	 */
	ExtensionManager getExtensionManager();

	/**
	 * Gets the plugin manager.
	 *
	 * @return the plugin manager
	 */
	PluginManager getPluginManager();

	/**
	 * Reload plugins.
	 */
	void reloadPlugins();

}