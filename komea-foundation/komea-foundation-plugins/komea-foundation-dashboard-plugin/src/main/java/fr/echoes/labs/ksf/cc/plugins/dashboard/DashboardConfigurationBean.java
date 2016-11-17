package fr.echoes.labs.ksf.cc.plugins.dashboard;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DashboardConfigurationBean {

	@Value("${ksf.dashboard.url}")
    private String url = "https://ksf-demo.metrixware.local/";

	@Value("${ksf.dashboard.organization.url}")
    private String organizationUrl = "https://ksf-demo.metrixware.local/organization";

	@Value("${ksf.dashboard.metrics.url}")
    private String metricsURL = "http://dashboard-demo.metrixware.local:8092/";

	@Value("${ksf.dashboard.timeseries.url}")
    private String timeSerieURL = "https://ksf-demo.metrixware.local/timeseries/";

	@Value("${ksf.dashboard.projectType:project}")
    private String projectType = "project";

	@Value("${ksf.dashboard.jobType:job}")
    private String jobType = "job";

	@Value("${ksf.dashboard.repositoryType:repository}")
    private String repositoryType = "repository";

	@Value("${ksf.dashboard.username}")
    private String username = "";

	@Value("${ksf.dashboard.password}")
    private String password = "";

	@Value("${ksf.dashboard.liferay.protocol}")
    private String liferayProtocol = "https";

	@Value("${ksf.dashboard.liferay.host}")
    private String liferayHost = "ksf-demo.metrixware.local";

	@Value("${ksf.dashboard.liferay.defaultTemplateName}")
    private String liferayDefaultTemplateName = "Komea Site Template";

	@Value("${ksf.dashboard.liferay.defaultCompanyWebId}")
    private String liferayDefaultCompanyWebId = "liferay.com";

	@Value("${ksf.dashboard.liferay.defaultUserGroupName}")
    private String liferayDefaultUserGroupName = "komea";

	@Value("${ksf.dashboard.redmineProjectTag}")
    private String redmineProjectTag = "redmineProject";

	@Value("${ksf.dashboard.metrics.averageTimeOnSite}")
    private Boolean calculateAverageTimeOnSite;

    public String getUrl() {
        if (url == null) {
            return url;
        }
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

    @JsonIgnore
    public String getProjectKeyTag() {
        return projectType + "_key";
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
    
    public void setCalculateAverageTimeOnSite(final Boolean calculateAverageTimeOnSite) {
    	this.calculateAverageTimeOnSite = calculateAverageTimeOnSite;
    }

    public Boolean getCalculateAverageTimeOnSite() {
        return calculateAverageTimeOnSite;
    }

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setOrganizationUrl(String organizationUrl) {
		this.organizationUrl = organizationUrl;
	}
	
	public void setMetricsURL(String metricsURL) {
		this.metricsURL = metricsURL;
	}
	
	public void setTimeSerieURL(String timeSerieURL) {
		this.timeSerieURL = timeSerieURL;
	}
	
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	
	public void setRepositoryType(String repositoryType) {
		this.repositoryType = repositoryType;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public void setLiferayProtocol(String liferayProtocol) {
		this.liferayProtocol = liferayProtocol;
	}

	public void setLiferayHost(String liferayHost) {
		this.liferayHost = liferayHost;
	}

	public void setLiferayDefaultTemplateName(String liferayDefaultTemplateName) {
		this.liferayDefaultTemplateName = liferayDefaultTemplateName;
	}

	public void setLiferayDefaultCompanyWebId(String liferayDefaultCompanyWebId) {
		this.liferayDefaultCompanyWebId = liferayDefaultCompanyWebId;
	}

	public void setLiferayDefaultUserGroupName(String liferayDefaultUserGroupName) {
		this.liferayDefaultUserGroupName = liferayDefaultUserGroupName;
	}
	
	public void setRedmineProjectTag(String redmineProjectTag) {
		this.redmineProjectTag = redmineProjectTag;
	}
	
}
