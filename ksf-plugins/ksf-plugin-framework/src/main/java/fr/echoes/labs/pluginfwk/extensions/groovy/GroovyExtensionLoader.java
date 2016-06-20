package fr.echoes.labs.pluginfwk.extensions.groovy;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;
import fr.echoes.labs.pluginfwk.api.plugin.PluginScanner;

/**
 * The Class GroovyExtensionLoader.
 */
public class GroovyExtensionLoader implements PluginScanner {

	private static final Logger						LOGGER	= LoggerFactory.getLogger(GroovyExtensionLoader.class);

	private final GroovyExtensionWrapperFactoryImpl	groovyPluginWrapperFactoryImpl;

	private final GroovyPluginConfiguration			groovyConfiguration;

	/**
	 * Instantiates a new groovy extension loader.
	 *
	 * @param groovyExtensionWrapperFactoryImpl
	 *            the groovy extension wrapper factory impl
	 * @param _groovyConfiguration
	 *            the _groovy configuration
	 */
	public GroovyExtensionLoader(final GroovyExtensionWrapperFactoryImpl groovyExtensionWrapperFactoryImpl,
			final GroovyPluginConfiguration _groovyConfiguration) {
		super();
		this.groovyPluginWrapperFactoryImpl = groovyExtensionWrapperFactoryImpl;
		this.groovyConfiguration = _groovyConfiguration;
	}

	@Override
	public String getName() {
		return "Groovy PluginScanner";
	}

	/**
	 * Load groovy plugçins from a specific folder and register the associated extensions.
	 */
	@Override
	public void reloadPlugins(final PluginManager _manager, final ClassLoader _parentClassLoader) {

		final File groovyPluginFolder = this.groovyConfiguration.getGroovyPluginFolder();
		LOGGER.info("Loading Groovy plugins from folder {}", groovyPluginFolder);
		if (!groovyPluginFolder.exists() || !groovyPluginFolder.isDirectory()) {
			LOGGER.error("Could not load Groovy plugins, the path provided is invalid ", groovyPluginFolder.getAbsolutePath());
			return;
		}
		final Collection<File> groovyPluginFiles = FileUtils.listFiles(groovyPluginFolder, new String[] { "groovy" }, false); //$NON-NLS-1$
		for (final File groovyPluginFile : groovyPluginFiles) {
			LOGGER.info("Loading the GROOVY extension {}", groovyPluginFile);
			try {
				final PluginDefinition pluginDefinition = this.groovyPluginWrapperFactoryImpl.loadGroovyPlugin(groovyPluginFile);

				_manager.registerPlugin(pluginDefinition);

			} catch (final Exception e) {
				LOGGER.error("Could not load the groovy plugin {}", groovyPluginFile, e);
				// this.eventBus.dispatchErrorEvent(e);
				// this.eventBus.dispatchEvent(new CouldNotLoadExtensionException("GROOVY", groovyPluginFile.getName()));
			}

		}
	}

}
