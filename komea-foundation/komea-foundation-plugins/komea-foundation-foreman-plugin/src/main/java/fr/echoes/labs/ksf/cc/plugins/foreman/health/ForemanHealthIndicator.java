package fr.echoes.labs.ksf.cc.plugins.foreman.health;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

import fr.echoes.labs.foremanapi.IForemanApi;
import fr.echoes.labs.foremanclient.ForemanClient;
import fr.echoes.labs.foremanclient.ForemanService;
import fr.echoes.labs.ksf.cc.plugins.foreman.exceptions.ForemanConfigurationException;
import fr.echoes.labs.ksf.cc.plugins.foreman.services.ForemanConfigurationService;

@Component
public class ForemanHealthIndicator extends AbstractHealthIndicator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ForemanHealthIndicator.class);
	
	@Autowired
	private ForemanConfigurationService configurationService;
	
	@Override
	protected void doHealthCheck(Builder builder) throws Exception {
		
		try {
			
			String url = configurationService.getForemanUrl();
			
			if (StringUtils.isEmpty(url)) {
				throw new ForemanConfigurationException("missing property url");
			}
			
			String username = configurationService.getForemanUsername();
			
			if (StringUtils.isEmpty(username)) {
				throw new ForemanConfigurationException("missing property username");
			}
			
			String password = configurationService.getForemanPassword();
			
			if (StringUtils.isEmpty(password)) {
				throw new ForemanConfigurationException("missing property password");
			}
			
			IForemanApi foremanClient = ForemanClient.createApi(url, username, password);

			foremanClient.getUsers("10");
			
			builder.up();
			
		} catch(Exception ex) {
			
			LOGGER.error("[Foreman] Health Indicator Error", ex);
			builder.down().withException(ex);
		}
	}

}
