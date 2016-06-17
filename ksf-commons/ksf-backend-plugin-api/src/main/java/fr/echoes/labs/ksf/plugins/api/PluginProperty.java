package fr.echoes.labs.ksf.plugins.api;

public class PluginProperty {
	private String	key;
	private String	name;
	private String	defaultValue ="";
	
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
	
	@Override
	public String toString() {
		return "PluginProperty [key=" + key + ", name=" + name + ", defaultValue=" + defaultValue + "]";
	}
}
