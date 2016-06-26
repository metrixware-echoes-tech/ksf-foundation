package fr.echoes.labs.pluginfwk.api.scanner;

import fr.echoes.labs.pluginfwk.api.plugin.PluginException;
import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;

public interface PluginScanner {

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	String getName();

	/**
	 * Reload plugins.
	 *
	 * @param _manager
	 *            the _manager
	 * @param _parentClassLoader
	 *            the _parent class loader
	 */
	void reloadPlugins(PluginManager _manager, ClassLoader _parentClassLoader) throws PluginException;

}
