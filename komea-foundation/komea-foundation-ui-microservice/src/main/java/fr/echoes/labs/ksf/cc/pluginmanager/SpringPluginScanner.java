package fr.echoes.labs.ksf.cc.pluginmanager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.google.common.base.Strings;

import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginException;
import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;
import fr.echoes.labs.pluginfwk.extensions.java.ClassLoaderAutoCloseable;
import fr.echoes.labs.pluginfwk.extensions.java.JavaPluginScanner;
import fr.echoes.labs.pluginfwk.extensions.java.KSFURLClassLoader;
import fr.echoes.labs.pluginfwk.pluginloader.PluginFrameworkConfiguration;

public class SpringPluginScanner extends JavaPluginScanner {

	private static final String			PLUGIN_PROPERTIES	= "ksfplugin.properties";
	private final ApplicationContext	webApplicationContext;

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

			final AnnotationConfigApplicationContext annotationConfigApplicationContext = this.initSpringEnv(ksfurlClassLoader);
			final PluginDefinition pluginDefinition = annotationConfigApplicationContext.getBean(PluginDefinition.class);
			this.LOGGER.info("Register plugin {}", pluginDefinition.getName());
			_manager.registerPlugin(pluginDefinition);
		} catch (final Exception ex) {
			this.LOGGER.error("Could not load the plugin : {}", ex.getMessage(), ex);
			throw new PluginException("Could not load the plugin", ex);
		} finally {

			Thread.currentThread().setContextClassLoader(backupCL);
		}
		return ClassLoaderAutoCloseable.MANUALLY;
	}

	AnnotationConfigApplicationContext initSpringEnv(final KSFURLClassLoader ksfurlClassLoader) throws IOException {
		final Properties properties = new Properties();
		final URL resource = ksfurlClassLoader.getResource(PLUGIN_PROPERTIES);
		if (resource == null) {
			throw new PluginMissingInformationException("Could not retrieve the plugin.properties file");
		}
		try (InputStream is = resource.openStream()) {
			properties.load(is);
		} catch (final IOException e) {
			throw new PluginMissingInformationException("Could not load the properties", e);

		}

		final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.setClassLoader(ksfurlClassLoader);
		annotationConfigApplicationContext.setParent(this.webApplicationContext);

		final String scannedPackage = properties.getProperty("scan");
		if (Strings.isNullOrEmpty(scannedPackage)) {
			throw new PluginMissingInformationException("Missing the component.scan property.");
		}
		this.LOGGER.warn("Scanning package {}", scannedPackage);
		annotationConfigApplicationContext.scan(scannedPackage);

		annotationConfigApplicationContext.refresh();
		annotationConfigApplicationContext.start();
		return annotationConfigApplicationContext;
	}
}
