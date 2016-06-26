package fr.echoes.labs.ksf.cc.pluginmanager;

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
import org.springframework.stereotype.Component;

import fr.echoes.labs.pluginfwk.api.extension.ExtensionManager;
import fr.echoes.labs.pluginfwk.api.plugin.PluginFramework;
import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;
import fr.echoes.labs.pluginfwk.extensions.java.JavaClasspathPluginScanner;
import fr.echoes.labs.pluginfwk.extensions.java.JavaPluginScanner;
import fr.echoes.labs.pluginfwk.pluginloader.PluginFrameworkImpl;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PluginManagerFactoryBean implements ApplicationContextAware {

	private static final Logger				LOGGER	= LoggerFactory.getLogger(PluginManagerFactoryBean.class);

	public PluginFrameworkConfigurationBean	pluginFrameworkConfiguration;

	private PluginFrameworkImpl				pluginFrameworkImpl;

	private ApplicationContext				applicationContext;

	private final PluginPropertyStorage		pluginPropertyStorage;

	@Autowired
	public PluginManagerFactoryBean(final PluginFrameworkConfigurationBean pluginFrameworkConfiguration, final PluginPropertyStorage pluginPropertyStorage) {
		super();
		this.pluginFrameworkConfiguration = pluginFrameworkConfiguration;
		this.pluginPropertyStorage = pluginPropertyStorage;
	}

	@PreDestroy
	public void destroy() throws IOException {
		if (this.pluginFrameworkImpl != null) {
			this.pluginFrameworkImpl.close();
		}
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public ExtensionManager extensionManager(final PluginFramework _pluginFramework) {
		LOGGER.info("Initialization ExtensionManager");
		return _pluginFramework.getExtensionManager();

	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public PluginFramework pluginFrameworkImpl() {
		LOGGER.info("Initialization PluginFramework");
		this.pluginFrameworkImpl = new PluginFrameworkImpl(this.pluginFrameworkConfiguration, this.pluginPropertyStorage);
		this.pluginFrameworkImpl.declarePluginScanner(new JavaPluginScanner(this.pluginFrameworkConfiguration));
		this.pluginFrameworkImpl.declarePluginScanner(new SpringPluginScanner(this.pluginFrameworkConfiguration, this.applicationContext));

		this.pluginFrameworkImpl.declarePluginScanner(new JavaClasspathPluginScanner());

		return this.pluginFrameworkImpl;

	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public PluginManager pluginManager(final PluginFramework _pluginFramework) {
		LOGGER.info("Initialization PluginManager");
		return _pluginFramework.getPluginManager();

	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}
}
