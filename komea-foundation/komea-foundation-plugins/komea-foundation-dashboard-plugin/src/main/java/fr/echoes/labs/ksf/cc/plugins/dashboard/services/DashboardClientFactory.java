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

import fr.echoes.labs.ksf.cc.plugins.dashboard.DashboardConfigurationBean;

@Service
public class DashboardClientFactory {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardClientFactory.class);
	
	private DashboardConfigurationService configurationService;
	
	@Autowired
	public DashboardClientFactory(final DashboardConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
	
	public MetricsStorageClient metricStorageClient() {
		
		final DashboardConfigurationBean configuration = this.configurationService.getPluginConfigurationBean();
		
		final String url = configuration.getMetricsURL();
		final String username = configuration.getUsername();
		final String password = configuration.getPassword();
		
		LOGGER.info("Initializing metrics storage client to {} for user {}", url, username);
		return new MetricsStorageClient(url, username, password);
	}
	
	public OrganizationStorageClient organizationStorageClient() {
		
		final DashboardConfigurationBean configuration = this.configurationService.getPluginConfigurationBean();
		
		final String url = configuration.getOrganizationUrl();
		final String username = configuration.getUsername();
		final String password = configuration.getPassword();
		
		LOGGER.info("Initializing organization client to {} for user {}", url, username);
		return new OrganizationStorageClient(url, username, password);		
	}
	
	public ConnectorsConfigurationClient connectorsConfigurationClient() {
		
		final DashboardConfigurationBean configuration = this.configurationService.getPluginConfigurationBean();
		
		final String url = configuration.getMetricsURL();
		final String username = configuration.getUsername();
		final String password = configuration.getPassword();
		
		LOGGER.info("Initializing connector configuration client to {} for user {}", url, username);
		return new ConnectorsConfigurationClient(url, username, password);
	}
	
	public LiferaySoapClient liferaySoapClient() {
		
		final DashboardConfigurationBean configuration = this.configurationService.getPluginConfigurationBean();
		
		final String protocol = configuration.getLiferayProtocol();
		final String host = configuration.getLiferayHost();
		final String username = configuration.getUsername();
		final String password = configuration.getPassword();
		
		LOGGER.info("Initializing liferay soap client to {} for user {}", host, username);
		return new LiferaySoapClient(protocol, host, username, password);	
	}
	
	public TimeSerieStorageClient timeSerieStorageClient() {
		
		final DashboardConfigurationBean configuration = this.configurationService.getPluginConfigurationBean();
		
		final String host = configuration.getTimeSerieURL();
		final String username = configuration.getUsername();
		final String password = configuration.getPassword();
		
		if (host != null && host.startsWith("http")) {
			return new TimeSerieStorageClient(host, username, password);
		}
		
		return null; //return new TimeSeriesMessagesClient(username, password, host)
	}

}
