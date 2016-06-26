package fr.echoes.labs.pluginfwk.pluginloader;

import java.io.IOException;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.echoes.labs.pluginfwk.api.extension.ExtensionManager;
import fr.echoes.labs.pluginfwk.api.plugin.PluginFramework;
import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;
import fr.echoes.labs.pluginfwk.api.scanner.PluginScanner;

/**
 * The Class PluginFrameworkImpl is the main class to initialize and manipulate the KSF Framework.
 */
public class PluginFrameworkImpl implements PluginFramework {

	private static final Logger						LOGGER	= LoggerFactory.getLogger(PluginFrameworkImpl.class);

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

	private final PluginPropertyStorage				pluginPropertyStorage;

	private final PluginFrameworkConfiguration		pluginFrameworkConfiguration;

	/**
	 * Instantiates a new plugin framework.
	 * e pe
	 *
	 * @param _pluginFrameworkConfiguration
	 *            the _plugin framework configuration
	 */
	public PluginFrameworkImpl(final PluginFrameworkConfiguration _pluginFrameworkConfiguration, final PluginPropertyStorage _pluginPropertyStorage) {
		Validate.notNull(_pluginFrameworkConfiguration);
		Validate.notNull(_pluginPropertyStorage);
		this.pluginFrameworkConfiguration = _pluginFrameworkConfiguration;
		this.pluginPropertyStorage = _pluginPropertyStorage;
		this.extensionSearchResultsCache = new ExtensionSearchResultsCacheImpl();
		this.extensionEndPoints = new ExtensionEndPoints();
		this.extensionManager = new ExtensionManagerImpl(this.extensionEndPoints, this.extensionSearchResultsCache);
		this.pluginClassLoader = new PluginClassLoaderImpl(this.pluginFrameworkConfiguration);
		this.pluginManager = new PluginManagerImpl(this.extensionManager, this.pluginFrameworkConfiguration, this.pluginPropertyStorage);
		this.pluginScanners = new PluginScannerRegistry(this.pluginManager, this.pluginClassLoader);

	}

	/*
	 * (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.pluginfwk.pluginloader.PluginFramework#close()
	 */
	@Override
	public void close() throws IOException {
		LOGGER.info("Cleaning the plugin Manager, its plugins and extensions");
		this.pluginManager.cleanup();
		this.pluginScanners.cleanup();
		this.pluginClassLoader.close();
	}

	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.pluginfwk.pluginloader.PluginFramework#declarePluginScanner(fr.echoes.labs.pluginfwk.api.plugin.PluginScanner)
	 */
	@Override
	public void declarePluginScanner(final PluginScanner _pluginScanner) {
		Validate.notNull(_pluginScanner);
		this.pluginScanners.declareScanner(_pluginScanner);
	}

	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.pluginfwk.pluginloader.PluginFramework#getExtensionManager()
	 */
	@Override
	public ExtensionManager getExtensionManager() {
		return this.extensionManager;
	}

	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.pluginfwk.pluginloader.PluginFramework#getPluginManager()
	 */
	@Override
	public PluginManager getPluginManager() {
		return this.pluginManager;
	}

	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.pluginfwk.pluginloader.PluginFramework#reloadPlugins()
	 */
	@Override
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
