package fr.echoes.labs.ksf.cc.plugins.dashboard.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author dcollard
 *
 */
@Service("dashboardConfiguration")
public class DashboardConfigurationService {
	
	public final static String GIT_REPOSITORIES_PROPERTY = "gitRepositories";

    @Value("${ksf.dashboard.url}")
    private String url;
    
    @Value("${ksf.dashboard.displayPage:}")
    private String displayPage;
    
    @Value("${ksf.dashboard.organization.url}")
    private String organizationUrl;
    
    @Value("${ksf.dashboard.customPeriod:30}")
    private int customPeriod;
    
    @Value("${ksf.dashboard.projectType:project}")
    private String projectType;
    
    @Value("${ksf.dashboard.jobType:job}")
    private String jobType;
    
    @Value("${ksf.dashboard.repositoryType:repository")
    private String repositoryType;
    
    @Value("${ksf.dashboard.username}")
    private String username;
    
    @Value("${ksf.dashboard.password}")
    private String password;

	public String getUrl() {
        if ('/' == this.url.charAt(this.url.length() - 1)) {
            this.url = this.url.substring(0, this.url.length() - 1);
        }
        return this.url;
	}
	
	/**
	 * @return the dashboard time period in days (default value is 30 days). 
	 */
	public int getCustomPeriod() {
		return customPeriod;
	}
	
	public String getProjectType() {
		return projectType;
	}
	
	public String getJobType() {
		return jobType;
	}
	
	public String getOrganizationUrl() {
		return organizationUrl;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getProjectKeyTag() {
		return projectType+"_key";
	}
	
	public String getRepositoryType() {
		return repositoryType;
	}
	
	public String getDisplayPage() {
		return displayPage;
	}

}
