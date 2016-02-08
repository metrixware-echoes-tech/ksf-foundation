package fr.echoes.labs.ksf.cc.plugins.foreman.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import fr.echoes.labs.foremanapi.IForemanApi;
import fr.echoes.labs.foremanclient.ForemanClient;
import fr.echoes.labs.ksf.users.security.api.ICurrentUserService;

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
		//currentUserService = applicationContext.getBean(ICurrentUserService.class);
		
		String url = configurationService.getForemanUrl();
		//String username = currentUserService.getCurrentUserLogin();
		//String password = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
		String username = configurationService.getForemanUsername();
		String password = configurationService.getForemanPassword();
		
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
