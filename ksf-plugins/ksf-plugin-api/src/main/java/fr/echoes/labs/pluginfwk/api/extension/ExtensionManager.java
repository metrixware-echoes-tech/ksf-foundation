package fr.echoes.labs.pluginfwk.api.extension;

import java.util.List;

/**
 * The Interface ExtensionManager defines the component that has registered all the extensions provided by the plugins. You can obtain the collections of
 * implementation for an endpoint.
 */
public interface ExtensionManager {

	/**
	 * Cleanup.
	 */
	void cleanup();

	/**
	 * Find extensions based on an EndPoint interface.
	 * Requests are put in cache in that way only the first call is expensive.
	 *
	 * @param <T>
	 *            the generic type
	 * @param _endPoint
	 *            the _end point
	 * @return the list
	 */
	<T extends IExtension> List<T> findExtensions(Class<T> _endPoint);

	/**
	 * Register extensions.
	 *
	 * @param pluginID
	 *            the plugin id
	 * @param extensions
	 *            the extensions
	 */
	void registerExtensions(String pluginID, IExtension[] extensions);

	/**
	 * Removes the extensions.
	 *
	 * @param pluginID
	 *            the plugin id
	 */
	void removeExtensions(String pluginID);

}