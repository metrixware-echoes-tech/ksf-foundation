package fr.echoes.labs.ksf.cc.plugins.dashboard.services;

import org.komea.liferay.client.LiferaySoapClient;
import org.komea.liferay.client.model.KomeaLiferaySite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardLiferayService {

	@Autowired
	private DashboardConfigurationService configuration;
	
	@Autowired
	private DashboardClientFactory clientFactory;
	
	public void createSite(final String projectKey) throws Exception {
		
		LiferaySoapClient liferay = clientFactory.liferaySoapClient();
		
		KomeaLiferaySite site = new KomeaLiferaySite();
		site.setSiteName(projectKey);
		site.setSiteURL("/"+projectKey);
		site.setSiteDescription(projectKey);
		site.setTemplateName(configuration.getLiferayDefaultTemplateName());
		site.setUserGroupName(configuration.getLiferayDefaultUserGroupName());
		site.setCompanyWebId(configuration.getLiferayDefaultCompanyWebId());

		String entityName = configuration.getProjectType()+"_"+projectKey;
		site.setSelectorEntities(entityName);
		
		liferay.createSite(site);
		
	}
	
}
