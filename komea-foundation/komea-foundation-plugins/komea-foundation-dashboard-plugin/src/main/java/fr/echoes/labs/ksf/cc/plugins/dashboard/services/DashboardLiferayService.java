package fr.echoes.labs.ksf.cc.plugins.dashboard.services;

import org.komea.liferay.client.LiferaySoapClient;
import org.komea.liferay.client.model.KomeaLiferaySite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tocea.corolla.products.domain.Project;

import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

@Service
public class DashboardLiferayService {
	
	@Autowired
	private DashboardConfigurationService configuration;
	
	@Autowired
	private DashboardClientFactory clientFactory;
	
	@Autowired
	private DashboardEntityFactory entityFactory;
	
	public String getLiferaySiteName(final ProjectDto project) {
		return project.getName();
	}
	
	public String getLiferaySiteName(final Project project) {
		return project.getName();
	}
	
	public void createSite(final ProjectDto project) throws Exception {
		
		LiferaySoapClient liferay = clientFactory.liferaySoapClient();
		final String projectKey = entityFactory.getProjectEntityKey(project);
		final String siteName = getLiferaySiteName(project);
		
		KomeaLiferaySite site = new KomeaLiferaySite();
		site.setSiteName(siteName);
		site.setSiteURL("/"+siteName);
		site.setSiteDescription(siteName);
		site.setTemplateName(configuration.getLiferayDefaultTemplateName());
		site.setUserGroupName(configuration.getLiferayDefaultUserGroupName());
		site.setCompanyWebId(configuration.getLiferayDefaultCompanyWebId());

		String entityName = configuration.getProjectType()+"_"+projectKey;
		site.setSelectorEntities(entityName);
		
		liferay.createSite(site);
		
	}
	
	public void deleteSite(final ProjectDto project) throws Exception {
		
		final String siteName = getLiferaySiteName(project);
		LiferaySoapClient liferay = clientFactory.liferaySoapClient();
		
		liferay.deleteSite(siteName, configuration.getLiferayDefaultCompanyWebId());
		
	}
	
}
