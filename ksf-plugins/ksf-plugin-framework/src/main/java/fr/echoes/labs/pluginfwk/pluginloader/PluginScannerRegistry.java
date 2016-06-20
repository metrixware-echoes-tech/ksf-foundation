package fr.echoes.labs.pluginfwk.pluginloader;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;
import fr.echoes.labs.pluginfwk.api.plugin.PluginScanner;

public class PluginScannerRegistry {

	private static final Logger			LOGGER			= LoggerFactory.getLogger(PluginScannerRegistry.class);

	private final PluginScanners		pluginScanners	= new PluginScanners();

	private final PluginManager			pluginManager;

	private final PluginClassLoaderImpl	pluginClassLoader;

	/**
	 * Instantiates a new plugin scanner registry.
	 *
	 * @param pluginManager
	 *            the plugin manager
	 * @param pluginClassLoader
	 *            the plugin class loader
	 */
	public PluginScannerRegistry(final PluginManager pluginManager, final PluginClassLoaderImpl pluginClassLoader) {
		super();
		this.pluginManager = pluginManager;
		this.pluginClassLoader = pluginClassLoader;
		Validate.notNull(pluginManager);
		Validate.notNull(pluginClassLoader);
	}

	/**
	 * Cleanup.
	 */
	public void cleanup() {
		this.pluginScanners.clear();

	}

	/**
	 * Declares a plugin scanner.
	 *
	 * @param _pluginScanner
	 *            the _plugin scanner
	 */
	public void declareScanner(final PluginScanner _pluginScanner) {
		Validate.notNull(_pluginScanner);
		LOGGER.info("Registering the plugin scanner {}", _pluginScanner.getName());
		this.pluginScanners.add(_pluginScanner);

	}

	public void scanForPlugins() {
		for (final PluginScanner pluginScanner : this.pluginScanners) {
			pluginScanner.reloadPlugins(this.pluginManager, this.pluginClassLoader.getClassLoader());
		}

	}

}
