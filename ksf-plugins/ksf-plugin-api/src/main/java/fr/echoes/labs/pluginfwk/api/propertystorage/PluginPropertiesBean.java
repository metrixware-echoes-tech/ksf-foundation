/*
 *
 */
package fr.echoes.labs.pluginfwk.api.propertystorage;

import java.util.Map;

/**
 * The Interface PluginPropertiesBean.
 */
public interface PluginPropertiesBean {

	/**
	 * Convert the properties as a bean.
	 *
	 * @param <T>
	 *            the generic type
	 * @param _bean
	 *            the bean implementation.
	 * @return the bean with the injected properties.
	 */
	<T> T convertAsBean(Class<T> _bean);

	/**
	 * Gets the plugin properties as map.
	 *
	 * @return the plugin properties as map
	 */
	Map<String, Object> getPluginPropertiesAsMap();

}
