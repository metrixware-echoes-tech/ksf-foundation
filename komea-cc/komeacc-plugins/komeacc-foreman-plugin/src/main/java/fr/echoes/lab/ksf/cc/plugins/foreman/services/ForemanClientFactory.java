package fr.echoes.lab.ksf.cc.plugins.foreman.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import fr.echoes.lab.foremanapi.IForemanApi;
import fr.echoes.lab.foremanclient.ForemanClient;
import fr.echoes.lab.ksf.users.security.api.ICurrentUserService;

@Component
public class ForemanClientFactory {

	@Autowired
	private ForemanConfigurationService configurationService;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private ICurrentUserService currentUserService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ForemanClientFactory.class);
	
	public IForemanApi createForemanClient() throws Exception {
		
		// Manually inject the bean for breaking a circular dependency issue
		currentUserService = applicationContext.getBean(ICurrentUserService.class);
		
		String url = configurationService.getForemanUrl();
		String username = currentUserService.getCurrentUserLogin();
		String password = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
		
		LOGGER.info("[foreman-plugin] init foreman client api for user {} on {}", username, url);
			
		IForemanApi foremanAPI;
		
		try {
			foremanAPI = ForemanClient.createApi(url, username, password);
		}catch(Exception e) {
			LOGGER.error("[foreman-plugin] error while initializing foreman client api : {}", e);
			throw e;
		}
		
		return foremanAPI;
	}
	
}
