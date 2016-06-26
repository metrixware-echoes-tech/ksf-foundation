package fr.echoes.labs.ksf.plugins.api;

import java.util.Collections;
import java.util.Map;

import fr.echoes.labs.pluginfwk.api.plugin.PluginPropertiesAndDescription;

/**
 * The Class PluginPropertiesAndDescriptionImpl ddefines a basic bean to handle the plugin properties.
 */
public class PluginPropertiesAndDescriptionImpl implements PluginPropertiesAndDescription {

	private final Map<String, String>	properties;

	private final Map<String, String>	descriptions;

	/**
	 * Instantiates a new plugin properties impl.
	 *
	 * @param properties
	 *            the properties
	 * @param descriptions
	 *            the descriptions
	 */
	public PluginPropertiesAndDescriptionImpl(final Map<String, String> properties, final Map<String, String> descriptions) {
		super();
		this.properties = properties;
		this.descriptions = descriptions;
	}

	@Override
	public Map<String, String> getDefaultPluginProperties() {
		return Collections.unmodifiableMap(this.properties);
	}

	@Override
	public String getDescription(final String propertyID) {
		return this.descriptions.get(propertyID);
	}

}
