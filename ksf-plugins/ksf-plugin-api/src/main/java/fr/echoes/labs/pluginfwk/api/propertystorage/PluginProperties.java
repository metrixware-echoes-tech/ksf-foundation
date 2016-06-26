package fr.echoes.labs.pluginfwk.api.propertystorage;

import java.util.Map;

public interface PluginProperties {

	/**
	 * Gets the plugin ID.
	 *
	 * @return the plugin ID
	 */
	String getPluginID();

	/**
	 * Gets the plugin properties.
	 *
	 * @return the plugin properties
	 */
	Map<String, ?> getPluginProperties();

}
