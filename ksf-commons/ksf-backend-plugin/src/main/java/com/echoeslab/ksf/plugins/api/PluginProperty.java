package com.echoeslab.ksf.plugins.api;

public class PluginProperty {
	private String	key;
	private String	name;
	private String	defaultValue;
	private String	type;

	public PluginProperty() {
		super();
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param _defaultValue
	 *            the defaultValue to set
	 */
	public void setDefaultValue(final String _defaultValue) {
		defaultValue = _defaultValue;
	}

	/**
	 * @param _key
	 *            the key to set
	 */
	public void setKey(final String _key) {
		key = _key;
	}

	/**
	 * @param _name
	 *            the name to set
	 */
	public void setName(final String _name) {
		name = _name;
	}

	/**
	 * @param _type
	 *            the type to set
	 */
	public void setType(final String _type) {
		type = _type;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PluginProperty [key=" + key + ", name=" + name + ", defaultValue=" + defaultValue + ", type=" + type
				+ "]";
	}
}
