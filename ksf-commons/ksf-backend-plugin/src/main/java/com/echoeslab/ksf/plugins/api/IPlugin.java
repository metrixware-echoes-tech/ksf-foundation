package com.echoeslab.ksf.plugins.api;

import java.io.Closeable;

/**
 * This interface declares a plugin and the informations that it should provide.
 * @author sleroy
 *
 */
public interface IPlugin extends Closeable {
	/*
	 * Returns the plugin description.
	 * @return the plugin description.
	 */
	String getDescription();

	/**
	 * Returns the plugin id
	 * @return the plugin ID.
	 */
	String getId();

	/**
	 * Returns the properties offered by the plugin.
	 * @return
	 */
	IPluginProperties getPluginProperties();
	
	/**
	 * Returns the summary into the HTML format.
	 * @return the summary into HTML Format.
	 */
	String getSummary();
}
