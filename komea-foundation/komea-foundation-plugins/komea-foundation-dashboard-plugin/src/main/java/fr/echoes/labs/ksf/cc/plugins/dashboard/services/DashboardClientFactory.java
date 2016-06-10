package fr.echoes.labs.ksf.cc.plugins.dashboard.services;

import org.komea.connectors.configuration.client.ConnectorsConfigurationClient;
import org.komea.liferay.client.LiferaySoapClient;
import org.komea.organization.storage.client.OrganizationStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardClientFactory {
	
	@Autowired
	private DashboardConfigurationService configurationService;
	
	public OrganizationStorageClient organizationStorageClient() {
		
		final String url = this.configurationService.getOrganizationUrl();
		final String username = this.configurationService.getUsername();
		final String password = this.configurationService.getPassword();
		
		return new OrganizationStorageClient(url, username, password);		
	}
	
	public ConnectorsConfigurationClient connectorsConfigurationClient() {
		
		final String url = this.configurationService.getOrganizationUrl();
		final String username = this.configurationService.getUsername();
		final String password = this.configurationService.getPassword();
		
		return new ConnectorsConfigurationClient(url, username, password);
	}
	
	public LiferaySoapClient liferaySoapClient() {
		
		final String protocol = this.configurationService.getLiferayProtocol();
		final String host = this.configurationService.getLiferayHost();
		final String username = this.configurationService.getUsername();
		final String password = this.configurationService.getPassword();
		
		return new LiferaySoapClient(protocol, host, username, password);	
	}

}
