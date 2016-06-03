package fr.echoes.labs.ksf.cc.plugins.dashboard.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author dcollard
 *
 */
@Service("dashboardConfiguration")
public class DashboardConfigurationService {

    @Value("${ksf.dashboard.url}")
    private String url;
    
    @Value("${ksf.dashboard.customPeriod:30}")
    private int customPeriod;
    
    @Value("${ksf.dashboard.projectType}")
    private String projectType;

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

}
