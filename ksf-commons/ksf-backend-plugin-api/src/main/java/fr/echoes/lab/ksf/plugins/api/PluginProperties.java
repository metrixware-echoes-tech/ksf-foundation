package fr.echoes.lab.ksf.plugins.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

public class PluginProperties implements IPluginPropertiesDefinition {

	private final Map<String, PluginProperty> properties = new HashMap<>();

	public PluginProperties() {
		super();
	}

	/**
	 * Declares a new property and provides the pojo to modify it.
	 *
	 * @param _key
	 *            the property key
	 * @return the plugin
	 */
	public PluginProperty declareProperty(final String _key) {
		Validate.notEmpty(_key, "Key should not be empty");
		if (properties.containsKey(_key)) {
			throw new IllegalArgumentException("Property " + _key + " is already defined.");
		}
		final PluginProperty property = new PluginProperty();
		property.setKey(_key);
		properties.put(_key, property);
		return property;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.echoes.lab.ksf.plugins.api.IPluginPropertiesDefinition#getProperties()
	 */
	@Override
	public Collection<PluginProperty> getProperties() {
		return properties.values();
	}
}
