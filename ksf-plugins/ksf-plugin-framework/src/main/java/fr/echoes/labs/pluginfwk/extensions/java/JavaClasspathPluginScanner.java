package fr.echoes.labs.pluginfwk.extensions.java;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginException;
import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;
import fr.echoes.labs.pluginfwk.api.plugin.PluginScanner;
import fr.echoes.labs.pluginfwk.api.plugin.PluginScannerException;

public class JavaClasspathPluginScanner implements PluginScanner {

	private static final Logger LOGGER = LoggerFactory.getLogger(JavaClasspathPluginScanner.class);

	@Override
	public String getName() {
		return "Classpath ServiceLoader PluginScanner";
	}

	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.pluginfwk.api.plugin.PluginScanner#reloadPlugins(fr.echoes.labs.pluginfwk.api.plugin.PluginManager, java.lang.ClassLoader)
	 */
	@Override
	public void reloadPlugins(final PluginManager _manager, final ClassLoader _parentClassLoader) throws PluginException {
		try {
			final ServiceLoader<PluginDefinition> serviceLoader = ServiceLoader.load(PluginDefinition.class, _parentClassLoader);
			final Iterator<PluginDefinition> iterator = serviceLoader.iterator();
			while (iterator.hasNext()) {
				final PluginDefinition pluginDefinition = iterator.next();
				try {
					_manager.registerPlugin(pluginDefinition);
				} catch (final Exception e) {
					LOGGER.error("Could not load the plugin with definition {}", pluginDefinition.getName());
				}

			}
		} catch (final Exception e) {
			throw new PluginScannerException(this.getName(), e);
		}
	}

}
