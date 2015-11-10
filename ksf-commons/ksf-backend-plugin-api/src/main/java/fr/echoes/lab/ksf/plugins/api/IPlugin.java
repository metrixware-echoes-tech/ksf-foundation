package fr.echoes.lab.ksf.plugins.api;

import java.io.Closeable;
import java.util.List;

import fr.echoes.lab.ksf.extensions.api.IExtension;

/**
 * This interface declares a plugin and the informations that it should provide.
 *
 * @author sleroy
 *
 */
public interface IPlugin extends Closeable {
	
	/*
	 * Returns the plugin description.
	 *
	 * @return the plugin description.
	 */
	String getDescription();

	/**
	 * Returns the list of extensions offered by this plugin.
	 *
	 * @return the list of extensions offered by this plugin.
	 */
	List<IExtension> getExtensions();

	/**
	 * Returns the plugin id
	 *
	 * @return the plugin ID.
	 */
	String getId();

	/**
	 * Returns the properties offered by the plugin.
	 *
	 * @return the definition of the plugin properties
	 */
	IPluginPropertiesDefinition getPluginProperties();

	/**
	 * Returns the summary into the HTML format.
	 *
	 * @return the summary into HTML Format.
	 */
	String getSummary();

	/**
	 * Initializes the plugin.
	 */

	void init();
}
