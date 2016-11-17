package fr.echoes.labs.ksf.cc.plugins.nexus.services;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.echoes.labs.ksf.cc.extensions.gui.KomeaFoundationContext;
import fr.echoes.labs.ksf.cc.plugins.nexus.NexusConfigurationBean;
import fr.echoes.labs.ksf.cc.plugins.nexus.NexusPlugin;
import fr.echoes.labs.pluginfwk.api.plugin.PluginFramework;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;

/**
 * @author dcollard
 *
 */
@Service("nexusConfiguration")
public class NexusConfigurationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NexusConfigurationService.class);

	private NexusConfigurationBean configuration;
	private final PluginPropertyStorage pluginPropertyStorage;
	private final KomeaFoundationContext foundation;
	
	@Autowired
	public NexusConfigurationService(final PluginFramework pluginFramework, final KomeaFoundationContext foundation) {
		this.foundation = foundation;
		this.pluginPropertyStorage = pluginFramework.getPluginPropertyStorage();
	}
	
	@PostConstruct
	public void initProperties() {
		this.configuration = pluginPropertyStorage.readPluginProperties(NexusPlugin.ID, NexusConfigurationBean.class);
		foundation.completeProperties(this.configuration);
	}
	
	public NexusConfigurationBean getConfigurationBean() {
        return this.configuration;
	}
	
//    @Value("${ksf.nexus.url}")
//    private String url;
//
//    @Value("${ksf.foreman.username}")
//    private String username;
//
//    @Value("${ksf.foreman.password}")
//    private String password;
//
//	public String getUrl() {
//        if ('/' == this.url.charAt(this.url.length() - 1)) {
//            this.url = this.url.substring(0, this.url.length() - 1);
//        }
//        return this.url;
//	}
//
//	/**
//	 * @return the username
//	 */
//	public String getUsername() {
//		return this.username;
//	}
//
//	/**
//	 * @return the password
//	 */
//	public String getPassword() {
//		return this.password;
//	}

}
