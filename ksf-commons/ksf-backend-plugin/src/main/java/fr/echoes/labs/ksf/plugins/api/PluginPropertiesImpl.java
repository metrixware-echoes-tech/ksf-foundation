package fr.echoes.labs.ksf.plugins.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fr.echoes.labs.pluginfwk.api.plugin.PluginProperties;

public class PluginPropertiesImpl implements PluginProperties {

	private final Map<String, Object>	properties		= new HashMap<>();

	private final Map<String, String>	descriptions	= new HashMap<>();

	public PluginPropertiesImpl() {
		super();
	}

	@Override
	public String getDescription(final String propertyID) {
		return this.descriptions.get(propertyID);
	}

	@Override
	public Map<String, Object> getPluginProperties() {
		return Collections.unmodifiableMap(this.properties);
	}

	@Override
	public void updateProperty(final String _key, final Object _value) {
		this.properties.put(_key, _value);
	}
}
