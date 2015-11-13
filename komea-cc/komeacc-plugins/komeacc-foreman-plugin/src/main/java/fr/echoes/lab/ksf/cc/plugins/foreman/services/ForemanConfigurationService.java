package fr.echoes.lab.ksf.cc.plugins.foreman.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ForemanConfigurationService {

	@Value("${ksf.foreman.url}")
	private String url;
	
	public String getForemanUrl() {
		return url;
	}
	
}
