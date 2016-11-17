package fr.echoes.labs.ksf.cc.plugins.dashboard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.echoes.labs.ksf.cc.extensions.gui.KomeaFoundationContext;
import fr.echoes.labs.ksf.cc.plugins.dashboard.DashboardConfigurationBean;
import fr.echoes.labs.ksf.cc.plugins.dashboard.KomeaDashboardPlugin;
import fr.echoes.labs.pluginfwk.api.plugin.PluginFramework;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;

/**
 * @author dcollard
 *
 */
@Service("dashboardConfiguration")
public class DashboardConfigurationService {
	
	public final static String GIT_REPOSITORIES_PROPERTY = "gitRepositories";
	
	private final KomeaFoundationContext foundation;
	private final PluginPropertyStorage pluginPropertyStorage;
	
	private DashboardConfigurationBean configuration;

    @Autowired
    public DashboardConfigurationService(final PluginFramework pluginFramework, final KomeaFoundationContext foundation) {
        super();
        this.pluginPropertyStorage = pluginFramework.getPluginPropertyStorage();
        this.foundation = foundation;
    }

    public DashboardConfigurationBean getPluginConfigurationBean() {
    	
    	if (this.configuration == null) {
    		this.configuration = this.pluginPropertyStorage.readPluginProperties(KomeaDashboardPlugin.ID, DashboardConfigurationBean.class);
    		foundation.completeProperties(this.configuration);
    	}
   	
        return this.configuration;
    }
	
    /*
	public final static String GIT_REPOSITORIES_PROPERTY = "gitRepositories";

    @Value("${ksf.dashboard.url}")
    private String url;
    
    @Value("${ksf.dashboard.organization.url}")
    private String organizationUrl;
    
    @Value("${ksf.dashboard.metrics.url}")
    private String metricsURL;
    
    @Value("${ksf.dashboard.timeseries.url}")
    private String timeSerieURL;
    
    @Value("${ksf.dashboard.projectType:project}")
    private String projectType;
    
    @Value("${ksf.dashboard.jobType:job}")
    private String jobType;
    
    @Value("${ksf.dashboard.repositoryType:repository}")
    private String repositoryType;
    
    @Value("${ksf.dashboard.username}")
    private String username;
    
    @Value("${ksf.dashboard.password}")
    private String password;
    
    @Value("${ksf.dashboard.liferay.protocol}")
    private String liferayProtocol;
    
    @Value("${ksf.dashboard.liferay.host}")
    private String liferayHost;
    
    @Value("${ksf.dashboard.liferay.defaultTemplateName}")
    private String liferayDefaultTemplateName;
    
    @Value("${ksf.dashboard.liferay.defaultCompanyWebId}")
    private String liferayDefaultCompanyWebId;
    
    @Value("${ksf.dashboard.liferay.defaultUserGroupName}")
    private String liferayDefaultUserGroupName;
    
    @Value("${ksf.dashboard.redmineProjectTag}")
    private String redmineProjectTag;
    
    @Value("${ksf.dashboard.metrics.averageTimeOnSite:false}")
    private boolean calculateAverageTimeOnSite;

	public String getUrl() {
        if ('/' == this.url.charAt(this.url.length() - 1)) {
            this.url = this.url.substring(0, this.url.length() - 1);
        }
        return this.url;
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
	
	public String getMetricsURL() {
		return metricsURL;
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
	
	public String getLiferayProtocol() {
		return liferayProtocol;
	}
	
	public String getLiferayHost() {
		return liferayHost;
	}

	public String getLiferayDefaultTemplateName() {
		return liferayDefaultTemplateName;
	}
	
	public String getLiferayDefaultCompanyWebId() {
		return liferayDefaultCompanyWebId;
	}
	
	public String getLiferayDefaultUserGroupName() {
		return liferayDefaultUserGroupName;
	}
	
	public String getTimeSerieURL() {
		return timeSerieURL;
	}
	
	public String getRedmineProjectTag() {
		return redmineProjectTag;
	}
	
	public boolean calculateAverageTimeOnSite() {
		return calculateAverageTimeOnSite;
	}
	*/
}

