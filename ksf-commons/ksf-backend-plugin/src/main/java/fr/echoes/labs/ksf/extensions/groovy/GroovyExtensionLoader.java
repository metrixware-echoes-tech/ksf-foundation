package fr.echoes.labs.ksf.extensions.groovy;

import java.io.File;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tocea.corolla.cqrs.gate.IEventBus;

import fr.echoes.labs.pluginfwk.api.extension.CouldNotLoadExtensionException;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;

/**
 * The Class GroovyExtensionLoader.
 */
@Service
public class GroovyExtensionLoader {

	private static final Logger						LOGGER	= LoggerFactory.getLogger(GroovyExtensionLoader.class);

	private final GroovyConfiguration				groovyConfiguration;

	private final GroovyExtensionWrapperFactoryImpl	groovyPluginWrapperFactoryImpl;

	private final PluginManager					pluginManagerImpl;

	private final IEventBus							eventBus;

	@Autowired
	public GroovyExtensionLoader(final GroovyConfiguration groovyConfiguration, final GroovyExtensionWrapperFactoryImpl groovyExtensionWrapperFactoryImpl,
			final PluginManager pluginManagerImpl, final IEventBus eventBus) {
		super();
		this.groovyConfiguration = groovyConfiguration;
		this.groovyPluginWrapperFactoryImpl = groovyExtensionWrapperFactoryImpl;
		this.pluginManagerImpl = pluginManagerImpl;
		this.eventBus = eventBus;
	}

	/**
	 * Load groovy plugçins from a specific folder and register the associated extensions.
	 */
	@PostConstruct
	public void loadGroovyExtensions() {

		final File groovyPluginFolder = new File(this.groovyConfiguration.getGroovyPluginFolder());
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

				this.pluginManagerImpl.registerPlugin(pluginDefinition);

			} catch (final Exception e) {
				LOGGER.error("Could not load the groovy plugin {}", groovyPluginFile);
				this.eventBus.dispatchErrorEvent(e);
				this.eventBus.dispatchEvent(new CouldNotLoadExtensionException("GROOVY", groovyPluginFile.getName()));
			}

		}
	}

}
