package fr.echoes.labs.pluginfwk.api.propertystorage;

import java.io.File;

/**
 * The Interface PluginPropertyStorage defines the implementation of a service that stores/retrieves the plugin properties. This service is provided to the
 * plugin at the initialization.
 */
public interface PluginPropertyStorage {

	/**
	 * Gets the plugin file storage folder.
	 *
	 * @param pluginID
	 *            the plugin ID
	 * @return the plugin file storage folder
	 */
	File getPluginFileStorageFolder(String pluginID);

	/**
	 * Inits the default properties.
	 *
	 * @param _pluginProperties
	 *            the properties
	 */
	void initDefaultProperties(PluginProperties _pluginProperties);

	/**
	 * Read the plugin properties and returns a bean to manipulate them.
	 *
	 * @param _pluginID
	 *            the plugin ID
	 * @return the plugin properties
	 */
	<T> T readPluginProperties(String _pluginID);

	/**
	 * Update plugin properties.
	 *
	 * @param _pluginProperties
	 *            the plugin properties
	 */
	void updatePluginProperties(PluginProperties _pluginProperties);

}
