package com.echoeslab.ksf.plugins.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

public class PluginProperties implements IPluginProperties {

	private final Map<String, PluginProperty> properties = new HashMap<>();

	public PluginProperties() {
		super();
	}

	/**
	 * Declares a new property and provides the pojo to modify it.
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
	 * @see com.echoeslab.ksf.plugins.api.IPluginProperties#getProperties()
	 */
	@Override
	public Collection<PluginProperty> getProperties() {
		return properties.values();
	}
}