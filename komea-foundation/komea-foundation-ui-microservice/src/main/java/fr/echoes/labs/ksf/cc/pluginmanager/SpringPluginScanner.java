package fr.echoes.labs.ksf.cc.pluginmanager;

import java.io.File;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginException;
import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;
import fr.echoes.labs.pluginfwk.extensions.java.ClassLoaderAutoCloseable;
import fr.echoes.labs.pluginfwk.extensions.java.JavaPluginScanner;
import fr.echoes.labs.pluginfwk.extensions.java.KSFURLClassLoader;
import fr.echoes.labs.pluginfwk.pluginloader.PluginFrameworkConfiguration;

public class SpringPluginScanner extends JavaPluginScanner {

	private final ApplicationContext webApplicationContext;

	public SpringPluginScanner(final PluginFrameworkConfiguration scannerConfiguration, final ApplicationContext _webApplicationContext) {
		super(scannerConfiguration);
		this.webApplicationContext = _webApplicationContext;
	}

	@Override
	public String getName() {
		return "Spring Plugin Loader";
	}

	@Override
	protected File getPluginFolder() {
		return this.scannerConfiguration.getSpringPluginFolder();
	}

	@Override
	protected File getTemporaryFolder() {

		return new File(this.scannerConfiguration.getTemporaryPluginFolder(), "spring");
	}

	@Override
	protected boolean hasValidPluginFolder() {
		return this.scannerConfiguration.getSpringPluginFolder() != null && this.scannerConfiguration.getSpringPluginFolder().exists()
				&& this.scannerConfiguration.getSpringPluginFolder().isDirectory();
	}

	@Override
	protected ClassLoaderAutoCloseable initPluginDefinition(final PluginManager _manager, final KSFURLClassLoader ksfurlClassLoader) {
		final ClassLoader backupCL = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(ksfurlClassLoader);

			final Properties properties = new Properties();
			properties.load(ksfurlClassLoader.getResourceAsStream("plugin.properties"));

			final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
			annotationConfigApplicationContext.setClassLoader(ksfurlClassLoader);
			annotationConfigApplicationContext.setParent(this.webApplicationContext);

			final String scannedPackage = properties.getProperty("scan");
			this.LOGGER.warn("Scanning package {}", scannedPackage);
			annotationConfigApplicationContext.scan(scannedPackage);

			annotationConfigApplicationContext.refresh();
			annotationConfigApplicationContext.start();
			final PluginDefinition pluginDefinition = annotationConfigApplicationContext.getBean(PluginDefinition.class);
			this.LOGGER.info("Register plugin {}", pluginDefinition.getName());
			_manager.registerPlugin(pluginDefinition);
		} catch (final Exception ex) {
			this.LOGGER.error("Could not load the plugin", ex);
			throw new PluginException("Could not load the plugin", ex);
		} finally {

			Thread.currentThread().setContextClassLoader(backupCL);
		}
		return ClassLoaderAutoCloseable.MANUALLY;
	}
}
