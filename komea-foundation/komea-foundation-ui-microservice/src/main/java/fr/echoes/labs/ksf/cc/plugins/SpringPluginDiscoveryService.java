package fr.echoes.labs.ksf.cc.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginException;
import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;

@Service
public class SpringPluginDiscoveryService implements BeanPostProcessor {

	private static final Logger	LOGGER	= LoggerFactory.getLogger(SpringPluginDiscoveryService.class);

	private final PluginManager	pluginManager;

	@Autowired
	public SpringPluginDiscoveryService(final PluginManager pluginManager) {
		super();
		this.pluginManager = pluginManager;
	}

	@Override
	public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
		if (PluginDefinition.class.isAssignableFrom(bean.getClass())) {
			LOGGER.debug("Found the plugin {} in the Spring bean factory.. declaring it automatically", beanName);
			try {
				this.pluginManager.registerPlugin((PluginDefinition) bean);
			} catch (PluginException ex) {
				throw new FatalBeanException("Exception caught when registering plugin " + ((PluginDefinition) bean).getName(), ex);
			}
		}
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
		return bean;
	}
	
}
