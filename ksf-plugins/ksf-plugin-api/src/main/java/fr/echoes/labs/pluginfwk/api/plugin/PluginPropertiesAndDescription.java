package fr.echoes.labs.pluginfwk.api.plugin;

import java.util.Map;

public interface PluginPropertiesAndDescription {

	/**
	 * Gets the default plugin properties in read-only access.
	 *
	 * @return the default plugin properties in read-only
	 */
	Map<String, String> getDefaultPluginProperties();

	/**
	 * Gets the description.
	 *
	 * @param propertyID
	 *            the property id
	 * @return the description
	 */
	String getDescription(String propertyID);

}