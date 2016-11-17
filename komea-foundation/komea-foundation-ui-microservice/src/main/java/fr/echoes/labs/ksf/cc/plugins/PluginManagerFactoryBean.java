package fr.echoes.labs.ksf.cc.plugins;

import java.io.File;
import java.io.IOException;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.jca.context.SpringContextResourceAdapter;
import org.springframework.stereotype.Component;

import fr.echoes.labs.pluginfwk.api.extension.ExtensionManager;
import fr.echoes.labs.pluginfwk.api.plugin.PluginFramework;
import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;
import fr.echoes.labs.pluginfwk.api.plugin.PluginPropertyStorageConfiguration;
import fr.echoes.labs.pluginfwk.api.plugin.PluginScannerManager;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;
import fr.echoes.labs.pluginfwk.extensions.java.JavaClasspathPluginScanner;
import fr.echoes.labs.pluginfwk.extensions.java.JavaPluginScanner;
import fr.echoes.labs.pluginfwk.extensions.java.JavaPluginScannerConfiguration;
import fr.echoes.labs.pluginfwk.extensions.spring.SpringContextPluginScanner;
import fr.echoes.labs.pluginfwk.extensions.spring.SpringPluginScanner;
import fr.echoes.labs.pluginfwk.extensions.spring.SpringPluginScannerConfiguration;
import fr.echoes.labs.pluginfwk.pluginloader.DefaultPluginPropertyStorageImplementation;
import fr.echoes.labs.pluginfwk.pluginloader.PluginFrameworkImpl;
import fr.echoes.labs.pluginfwk.pluginloader.PluginPropertyStorageImpl;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PluginManagerFactoryBean implements ApplicationContextAware {

	private static final Logger				LOGGER	= LoggerFactory.getLogger(PluginManagerFactoryBean.class);

	public PluginFrameworkConfigurationBean	pluginFrameworkConfiguration;
	private JavaPluginScannerConfiguration javaPluginScannerConfiguration;
	private SpringPluginScannerConfiguration springPluginScannerConfiguration;

	private PluginFrameworkImpl				pluginFrameworkImpl;

	private ApplicationContext				applicationContext;

	@Autowired
	public PluginManagerFactoryBean(final PluginFrameworkConfigurationBean pluginFrameworkConfiguration) {
		super();
		this.pluginFrameworkConfiguration = pluginFrameworkConfiguration;
		this.javaPluginScannerConfiguration = new JavaPluginScannerConfiguration() {
			@Override
			public File getPluginFolder() {
				return pluginFrameworkConfiguration.getJavaPluginFolder();
			}
		};
		this.springPluginScannerConfiguration = new SpringPluginScannerConfiguration() {
			@Override
			public File getPluginFolder() {
				return pluginFrameworkConfiguration.getSpringPluginFolder();
			}
		};
	}

	@PreDestroy
	public void destroy() throws IOException {
		if (this.pluginFrameworkImpl != null) {
			LOGGER.info("Closing PluginFramework...");
			this.pluginFrameworkImpl.close();
		}
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public ExtensionManager extensionManager(final PluginFramework pluginFramework) {
		LOGGER.info("Initialization ExtensionManager...");
		return pluginFramework.getExtensionManager();
	}
	
	private PluginPropertyStorage pluginPropertyStorage() {
		
		final File location = this.pluginFrameworkConfiguration.getPluginPropertyStorageLocation();
		PluginPropertyStorageConfiguration config;
		
		if (location == null) {
			// use default location
			config = new DefaultPluginPropertyStorageImplementation();
		}else{
			// use custom location
			config = new DefaultPluginPropertyStorageImplementation(location);
		}
		
		return new PluginPropertyStorageImpl(config);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public PluginFramework pluginFrameworkImpl() {
		LOGGER.info("Initialization PluginFramework...");
		this.pluginFrameworkImpl = new PluginFrameworkImpl(this.pluginFrameworkConfiguration, pluginPropertyStorage());
		final PluginScannerManager scannerManager = this.pluginFrameworkImpl.getPluginScannerManager();
		scannerManager.declarePluginScanner(new JavaPluginScanner(this.pluginFrameworkConfiguration, this.javaPluginScannerConfiguration));
		scannerManager.declarePluginScanner(new SpringPluginScanner(this.pluginFrameworkConfiguration, this.springPluginScannerConfiguration, this.applicationContext));
		scannerManager.declarePluginScanner(new SpringContextPluginScanner(this.applicationContext));
//		scannerManager.declarePluginScanner(new JavaClasspathPluginScanner());
		return this.pluginFrameworkImpl;
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public PluginManager pluginManager(final PluginFramework pluginFramework) {
		LOGGER.info("Initialization PluginManager...");
		return pluginFramework.getPluginManager();
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
