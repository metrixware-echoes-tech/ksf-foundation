package fr.echoes.labs.pluginfwk.extensions.java;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginException;
import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;
import fr.echoes.labs.pluginfwk.api.scanner.PluginScanner;
import fr.echoes.labs.pluginfwk.api.scanner.PluginScannerException;
import fr.echoes.labs.pluginfwk.pluginloader.PluginFrameworkConfiguration;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class JavaPluginScanner implements PluginScanner {

	protected final Logger							LOGGER					= LoggerFactory.getLogger(this.getClass());

	protected final PluginFrameworkConfiguration	scannerConfiguration;

	protected final String[]						javaPluginExtensions	= { "zip" };

	protected List<KSFURLClassLoader>				ksfUrlClassLoaders		= new ArrayList();

	public JavaPluginScanner(final PluginFrameworkConfiguration scannerConfiguration) {
		super();
		this.scannerConfiguration = scannerConfiguration;
	}

	@Override
	public String getName() {
		return "Java plugin Scanner";
	}

	@Override
	public synchronized void reloadPlugins(final PluginManager _manager, final ClassLoader _parentClassLoader) throws PluginException {
		Validate.notNull(_manager);
		Validate.notNull(_parentClassLoader);
		this.closeUrlClassLoaders();

		if (!this.hasValidPluginFolder()) {
			this.LOGGER.error("Could not load the plugins, the folder {} is invalid.", this.getPluginFolder());
			return;
		}
		try {
			FileUtils.deleteDirectory(this.getTemporaryFolder());
		} catch (final IOException e1) {
			throw new PluginException("Could not clean the temporary directory " + this.getTemporaryFolder(), e1);
		}
		if (!this.getTemporaryFolder().mkdirs()) {
			this.LOGGER.warn("Could not create the temporary folder {}, maybe it already exists", this.getTemporaryFolder());

		}
		if (!this.scannerConfiguration.hasValidTemporaryPluginFolder()) {
			this.LOGGER.error("The temporary folder '{}' is invalid.", this.getTemporaryFolder());
		}

		try {
			final Collection<File> plugins = this.scanForPlugins();
			this.LOGGER.info("Plugins found {}", plugins.size());
			for (final File pluginZip : plugins) {
				try {

					final File pluginDestination = this.getPluginDestination(pluginZip);
					this.LOGGER.debug("Plugin installation folder", pluginDestination);
					if (pluginDestination.exists()) {
						this.LOGGER.warn("Cleaning the temporary folder for the plugin {}", pluginDestination);
						FileUtils.deleteDirectory(pluginDestination);
					} else {
						this.LOGGER.debug("Plugin folder needs to be created");
						Validate.isTrue(pluginDestination.mkdirs());
					}

					// Eveything is OK
					this.loadPluginDefinition(_parentClassLoader, pluginZip, pluginDestination, _manager);

				} catch (final Exception e) {
					this.LOGGER.error("Could not load the plugin {}", pluginZip, e);
					_manager.reportPluginFailure(new PluginException("Plugin " + pluginZip + " could not be loaded.", e));
				}
			}
		} catch (final Exception e) {
			throw new PluginScannerException(this.getName() + " has failed", e);
		}
	}

	/**
	 * Close url class loaders.
	 */
	private void closeUrlClassLoaders() {
		for (final KSFURLClassLoader cl : this.ksfUrlClassLoaders) {
			try {
				cl.close();
			} catch (final IOException e) {
				this.LOGGER.error("Could not close the classloader of {}", cl);
			}
		}
		this.ksfUrlClassLoaders.clear();

	}

	/**
	 * Gets the plugin destination. It basically appends the plugin name (without extension) to the temporary folder.
	 *
	 * @param pluginZip
	 *            the plugin zip
	 * @return the plugin destination
	 */
	private File getPluginDestination(final File pluginZip) {
		return new File(this.getTemporaryFolder(), FilenameUtils.removeExtension(pluginZip.getName()));
	}

	/**
	 * Load a plugin definition.
	 *
	 * @param _parentClassLoader
	 *            the parent class loader
	 * @param pluginZip
	 *            the plugin zip
	 * @param pluginDestination
	 *            the plugin destination
	 * @param _manager
	 *            the manager
	 * @throws ZipException
	 *             the zip exception
	 */
	@SuppressWarnings("nls")
	private void loadPluginDefinition(final ClassLoader _parentClassLoader, final File pluginZip, final File pluginDestination, final PluginManager _manager)
			throws ZipException {
		this.LOGGER.debug("Unzipping {} in {}", pluginZip, pluginDestination);
		final ZipFile zipFile = new ZipFile(pluginZip);
		zipFile.extractAll(pluginDestination.getAbsolutePath());
		this.LOGGER.debug("Scanning {} to find jars...", pluginDestination);
		final Collection<File> jarFiles = ScanUtils.scanForJars(pluginDestination);
		this.LOGGER.info("Found {} jars", jarFiles);
		KSFURLClassLoader ksfurlClassLoader = null;

		try {
			this.LOGGER.debug("Loading the Jars into a new classloader");
			ksfurlClassLoader = KSFURLClassLoader.newPluginClassLoader(_parentClassLoader, jarFiles);
			switch (this.initPluginDefinition(_manager, ksfurlClassLoader)) {
			case AUTO_CLOSE:
				break;
			case MANUALLY:
				this.LOGGER.debug("Added a classloader to the list {}", ksfurlClassLoader);
				this.ksfUrlClassLoaders.add(ksfurlClassLoader);
				break;
			}

		} catch (final Exception e) {
			this.LOGGER.error("Could not obtain a plugin definition", e);
			IOUtils.closeQuietly(ksfurlClassLoader);

		}
	}

	/**
	 * Scan for plugins.
	 *
	 * @return the collection
	 */
	private Collection<File> scanForPlugins() {
		return ScanUtils.scanForExtensions(this.getPluginFolder(), "zip");
	}

	/**
	 * Gets the plugin folder.
	 *
	 * @return the plugin folder
	 */
	protected File getPluginFolder() {
		return this.scannerConfiguration.getJavaPluginFolder();
	}

	/**
	 * Gets the temporary folder.
	 *
	 * @return the temporary folder
	 */
	protected File getTemporaryFolder() {
		return new File(this.scannerConfiguration.getTemporaryPluginFolder(), "java");
	}

	protected boolean hasValidPluginFolder() {
		return this.scannerConfiguration.getJavaPluginFolder() != null && this.scannerConfiguration.getJavaPluginFolder().exists()
				&& this.scannerConfiguration.getJavaPluginFolder().isDirectory();

	}

	/**
	 * Inits the plugin definition from the new classloader.
	 *
	 * @param _manager
	 *            the manager
	 * @param ksfurlClassLoader
	 *            the ksfurl class loader
	 * @return true, if the classloader will be autoclosed.
	 */
	protected ClassLoaderAutoCloseable initPluginDefinition(final PluginManager _manager, final KSFURLClassLoader ksfurlClassLoader) {
		final PluginDefinitionServiceLoader pluginDefinitionServiceLoader = new PluginDefinitionServiceLoader(ksfurlClassLoader);

		final PluginDefinition pluginDefinition = new AutoCloseClassLoaderPluginDefinition(ksfurlClassLoader,
				pluginDefinitionServiceLoader.getPluginDefinition());
		_manager.registerPlugin(pluginDefinition);
		return ClassLoaderAutoCloseable.AUTO_CLOSE;
	}

}
