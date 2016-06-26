/*
 *
 */
package fr.echoes.labs.pluginfwk.extensions.java;

import java.util.Iterator;
import java.util.ServiceLoader;

import com.google.common.collect.Iterators;

import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;

public class PluginDefinitionServiceLoader {

	private final ClassLoader classLoader;

	public PluginDefinitionServiceLoader(final ClassLoader _classLoader) {
		this.classLoader = _classLoader;

	}

	/**
	 * Gets the unique plugin definition from the service loader. No other plugin should have been declared;
	 *
	 * @return the plugin definition
	 */
	public PluginDefinition getPluginDefinition() {
		return Iterators.getOnlyElement(this.getPluginDefinitions());
	}

	/**
	 * Gets the plugin definitions.
	 *
	 * @return the plugin definitions
	 */
	public Iterator<PluginDefinition> getPluginDefinitions() {
		final ServiceLoader<PluginDefinition> serviceLoader = ServiceLoader.load(PluginDefinition.class, this.classLoader);
		final Iterator<PluginDefinition> iterator = serviceLoader.iterator();

		return iterator;
	}
}
