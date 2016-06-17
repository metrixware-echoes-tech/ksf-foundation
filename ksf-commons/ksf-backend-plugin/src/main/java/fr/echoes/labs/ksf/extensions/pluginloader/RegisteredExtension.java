package fr.echoes.labs.ksf.extensions.pluginloader;

import fr.echoes.labs.pluginfwk.api.extension.IExtension;

/**
 * The Interface IRegisteredExtension.
 */
public interface RegisteredExtension {

	/**
	 * Gets the extension.
	 *
	 * @return the extension
	 */
	IExtension getExtension();

	/**
	 * Gets the plugin id.
	 *
	 * @return the plugin id
	 */
	String getPluginID();
}
