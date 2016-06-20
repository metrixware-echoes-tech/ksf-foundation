package fr.echoes.labs.pluginfwk.pluginloader;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;
import fr.echoes.labs.pluginfwk.api.plugin.PluginScanner;

/**
 * The Class PluginFramework is the main class to initialize and manipulate the KSF Framework.
 */
public class PluginFramework implements Closeable {

	private static final Logger						LOGGER	= LoggerFactory.getLogger(PluginFramework.class);

	/** The extension search results cache. */
	private final ExtensionSearchResultsCacheImpl	extensionSearchResultsCache;

	/** The plugin manager. */
	private final PluginManager						pluginManager;

	/** The extension end points. */
	private final ExtensionEndPoints				extensionEndPoints;

	/** The extension manager. */
	private final ExtensionManagerImpl				extensionManager;

	/** The plugin scanners. */
	private final PluginScannerRegistry				pluginScanners;

	private final PluginClassLoaderImpl				pluginClassLoader;

	/**
	 * Instantiates a new plugin framework.
	 *
	 * @param _pluginFrameworkConfiguration
	 *            the _plugin framework configuration
	 */
	public PluginFramework(final PluginFrameworkConfiguration _pluginFrameworkConfiguration) {
		Validate.notNull(_pluginFrameworkConfiguration);
		this.extensionSearchResultsCache = new ExtensionSearchResultsCacheImpl();
		this.extensionEndPoints = new ExtensionEndPoints();
		this.extensionManager = new ExtensionManagerImpl(this.extensionEndPoints, this.extensionSearchResultsCache);
		this.pluginClassLoader = new PluginClassLoaderImpl(_pluginFrameworkConfiguration);
		this.pluginManager = new PluginManagerImpl(this.extensionManager, _pluginFrameworkConfiguration);
		this.pluginScanners = new PluginScannerRegistry(this.pluginManager, this.pluginClassLoader);

	}

	/*
	 * (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		LOGGER.info("Cleaning the plugin Manager, its plugins and extensions");
		this.pluginManager.cleanup();
		this.pluginScanners.cleanup();
		this.pluginClassLoader.close();
	}

	/**
	 * Declare plugin scanner.
	 *
	 * @param _pluginScanner
	 *            the plugin scanner
	 */
	public void declarePluginScanner(final PluginScanner _pluginScanner) {
		Validate.notNull(_pluginScanner);
		this.pluginScanners.declareScanner(_pluginScanner);
	}

	/**
	 * Reload plugins.
	 */
	public void reloadPlugins() {
		try {
			LOGGER.info("Reloads all the plugins");
			this.pluginManager.cleanup();
			this.pluginClassLoader.reloadClassLoader();
		} finally {
			this.pluginScanners.scanForPlugins();
		}
	}

}
