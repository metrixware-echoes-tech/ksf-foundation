package fr.echoes.labs.ksf.cc.plugins.dashboard.services;

import org.komea.connectors.configuration.client.ConnectorsConfigurationClient;
import org.komea.liferay.client.LiferaySoapClient;
import org.komea.metrics.client.MetricsStorageClient;
import org.komea.organization.storage.client.OrganizationStorageClient;
import org.komea.timeseries.client.TimeSerieStorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardClientFactory {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardClientFactory.class);
	
	@Autowired
	private DashboardConfigurationService configurationService;
	
	public MetricsStorageClient metricStorageClient() {
		
		final String url = this.configurationService.getMetricsURL();
		final String username = this.configurationService.getUsername();
		final String password = this.configurationService.getPassword();
		
		LOGGER.info("Initializing metrics storage client to {} for user {}", url, username);
		return new MetricsStorageClient(url, username, password);
	}
	
	public OrganizationStorageClient organizationStorageClient() {
		
		final String url = this.configurationService.getOrganizationUrl();
		final String username = this.configurationService.getUsername();
		final String password = this.configurationService.getPassword();
		
		LOGGER.info("Initializing organization client to {} for user {}", url, username);
		return new OrganizationStorageClient(url, username, password);		
	}
	
	public ConnectorsConfigurationClient connectorsConfigurationClient() {
		
		final String url = this.configurationService.getMetricsURL();
		final String username = this.configurationService.getUsername();
		final String password = this.configurationService.getPassword();
		
		LOGGER.info("Initializing connector configuration client to {} for user {}", url, username);
		return new ConnectorsConfigurationClient(url, username, password);
	}
	
	public LiferaySoapClient liferaySoapClient() {
		
		final String protocol = this.configurationService.getLiferayProtocol();
		final String host = this.configurationService.getLiferayHost();
		final String username = this.configurationService.getUsername();
		final String password = this.configurationService.getPassword();
		
		LOGGER.info("Initializing liferay soap client to {} for user {}", host, username);
		return new LiferaySoapClient(protocol, host, username, password);	
	}
	
	public TimeSerieStorageClient timeSerieStorageClient() {
		
		final String host = this.configurationService.getTimeSerieURL();
		final String username = this.configurationService.getUsername();
		final String password = this.configurationService.getPassword();
		
		if (host != null && host.startsWith("http")) {
			return new TimeSerieStorageClient(host, username, password);
		}
		
		return null; //return new TimeSeriesMessagesClient(username, password, host)
	}

}
