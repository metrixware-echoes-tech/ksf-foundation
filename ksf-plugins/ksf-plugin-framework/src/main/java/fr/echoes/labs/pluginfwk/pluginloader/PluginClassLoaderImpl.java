package fr.echoes.labs.pluginfwk.pluginloader;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.echoes.labs.pluginfwk.extensions.java.KSFURLClassLoader;

public class PluginClassLoaderImpl implements Closeable {

	private static final Logger					LOGGER	= LoggerFactory.getLogger(PluginClassLoaderImpl.class);

	private final PluginFrameworkConfiguration	pluginFrameworkConfiguration;

	private ClassLoader							ksfURLClassloader;

	/**
	 * Instantiates a new plugin class loader impl.
	 *
	 * @param pluginFrameworkConfiguration
	 *            the plugin framework configuration
	 */
	public PluginClassLoaderImpl(final PluginFrameworkConfiguration pluginFrameworkConfiguration) {
		super();
		this.pluginFrameworkConfiguration = pluginFrameworkConfiguration;

	}

	@Override
	public void close() throws IOException {
		this.closeClassLoader();
	}

	/**
	 * Close class loader.
	 */
	public void closeClassLoader() {
		if (this.ksfURLClassloader != null && KSFURLClassLoader.class.isAssignableFrom(this.ksfURLClassloader.getClass())) {
			LOGGER.error("Closing the custom classloader");
			try {
				((KSFURLClassLoader) this.ksfURLClassloader).close();
			} catch (final IOException e) {
				LOGGER.error("Exception when the custom KSF ClassLoader has been closed", e);
			}
		}

	}

	/**
	 * Gets the class loader.
	 *
	 * @return the class loader
	 */
	public ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * Reload class loader.
	 */
	@SuppressWarnings("nls")
	public void reloadClassLoader() {
		this.closeClassLoader();
		final File extraLibsFolder = this.pluginFrameworkConfiguration.getExtraLibsFolder();
		if (extraLibsFolder != null && extraLibsFolder.exists() && extraLibsFolder.isDirectory()) {
			LOGGER.info("Scanning a folder to load extra libraries");
			final Collection<File> jarFiles = this.listJarFiles(extraLibsFolder); // $NON-NLS-1$
			this.ksfURLClassloader = KSFURLClassLoader.newPluginClassLoader(Thread.currentThread().getContextClassLoader(), jarFiles);
		} else {
			LOGGER.warn("No custom library folder defined");
			this.ksfURLClassloader = Thread.currentThread().getContextClassLoader();
		}

	}

	private Collection<File> listJarFiles(final File extraLibsFolder) {
		return FileUtils.listFiles(extraLibsFolder, new String[] { "jar" }, true);
	}

}
