package fr.echoes.labs.pluginfwk.api.plugin;

import fr.echoes.labs.pluginfwk.api.extension.IExtension;

public interface PluginInformations {

	/**
	 * Returns the plugin description into HTML Format.
	 *
	 * @return the plugin description.
	 */
	String getDescription();

	/**
	 * Gets the extensions.
	 *
	 * @return the extensions
	 */
	IExtension[] getExtensions();

	/**
	 * Returns the plugin id
	 *
	 * @return the plugin ID.
	 */
	String getId();

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	String getName();

	/**
	 * Returns the properties offered by the plugin.
	 *
	 * @return the definition of the plugin properties
	 */
	Object getPluginProperties();

}