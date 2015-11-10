package com.echoeslab.ksf.plugins.api;

import java.util.List;

public interface IPluginRegistry {
	/**
	 * Returns the list of plugins.
	 * @return the list of loaded plugins.
	 */
	List<IPlugin> getPlugins();

	/**
	 * Registers a new plugin
	 * @param _plugin the plugin.
	 */
	void registerPlugin(IPlugin _plugin);
	/**
	 * Removes a plugin
	 * @param _pluginID the plugin ID
	 */
	void removePlugin(String _pluginID);
}
