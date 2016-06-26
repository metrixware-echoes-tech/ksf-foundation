package fr.echoes.labs.pluginfwk.extensions.java;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginException;
import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;
import fr.echoes.labs.pluginfwk.api.scanner.PluginScanner;
import fr.echoes.labs.pluginfwk.api.scanner.PluginScannerException;

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
			final PluginDefinitionServiceLoader pluginDefinitionServiceLoader = new PluginDefinitionServiceLoader(_parentClassLoader);
			final Iterator<PluginDefinition> pluginDefinitions = pluginDefinitionServiceLoader.getPluginDefinitions();
			while (pluginDefinitions.hasNext()) {
				final PluginDefinition pluginDefinition = pluginDefinitions.next();
				try {
					_manager.registerPlugin(pluginDefinition);
				} catch (final Exception e) {
					LOGGER.error("Could not load the plugin with definition {}", pluginDefinition.getName());
					_manager.reportPluginFailure(new PluginException("Could not obntain plugin from " + pluginDefinition.getId(), e));
				}

			}
		} catch (final Exception e) {
			throw new PluginScannerException(this.getName(), e);
		}
	}

}
