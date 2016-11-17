package fr.echoes.labs.ksf.cc.plugins;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.echoes.labs.pluginfwk.api.plugin.PluginException;
import fr.echoes.labs.pluginfwk.api.plugin.PluginFramework;

@Component
public class PluginFrameworkInitialisationBean {

	private Logger LOGGER = LoggerFactory.getLogger(PluginFrameworkInitialisationBean.class);
    
    @Autowired
    private PluginFramework pluginManager;
    
    @PostConstruct
    public void init() throws PluginException {
    	LOGGER.info("Loading plugins...");
        pluginManager.reloadPlugins();
    }
	
}
