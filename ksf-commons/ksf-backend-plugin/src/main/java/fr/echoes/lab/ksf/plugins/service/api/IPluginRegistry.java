package fr.echoes.lab.ksf.plugins.service.api;

import java.util.List;

import fr.echoes.lab.ksf.plugins.api.IPlugin;

/**
 * This interface defines the interface to manipulate the plugins.
 *
 * @author sleroy
 *
 */
public interface IPluginRegistry {
	/**
	 * Returns the list of plugins.
	 *
	 * @return the list of loaded plugins.
	 */
	List<IPlugin> getPlugins();

	/**
	 * Registers a new plugin
	 *
	 * @param _plugin
	 *            the plugin.
	 */
	void registerPlugin(IPlugin _plugin);

	/**
	 * Removes a plugin
	 *
	 * @param _pluginID
	 *            the plugin ID
	 */
	void removePlugin(String _pluginID);
}
