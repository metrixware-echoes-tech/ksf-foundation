package fr.echoes.labs.pluginfwk.api.plugin;

import java.util.Map;

public interface PluginProperties {

	/**
	 * Gets the description.
	 *
	 * @param propertyID
	 *            the property id
	 * @return the description
	 */
	String getDescription(String propertyID);

	/**
	 * Gets the plugin properties in read-only access.
	 *
	 * @return the plugin properties in read-only
	 */
	Map<String, Object> getPluginProperties();

	/**
	 * Updates a property.
	 *
	 * @param _key
	 *            the _key
	 * @param _value
	 *            the _value
	 */
	void updateProperty(String _key, Object _value);

}