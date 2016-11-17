package fr.echoes.labs.komea.foundation.plugins.jenkins;

import org.springframework.beans.factory.annotation.Value;

public class JenkinsConfigurationBean {

	@Value("${ksf.jenkins.url}")
    private String url;
    
    @Value("${ksf.jenkins.username:}")
    private String username;
    
    @Value("${ksf.jenkins.apiKey:}")
    private String apiKey;

    @Value("${ksf.jenkins.templateName:}")
    private String templateName;
    
    @Value("${ksf.jenkins.templateFolder:}")
    private String templateFolder;

    @Value("${ksf.scmUrl}")
    private String scmUrl;

    @Value("${ksf.jenkins.useFolders}")
    private boolean useFolders;

    @Value("${ksf.jenkins.builsdPerJobLimit}")
    private int builsdPerJobLimit;

    @Value("${ksf.buildSystem.defaultScript}")
    private String buildScript;

    @Value("${ksf.artifacts.publishScript}")
    private String publishScript;

    @Value("${ksf.projectScmUrlPattern}")
    private String projectScmUrlPattern;

    @Value("${ksf.jenkins.job.displayNamePattern}")
    private String jobDisplayNamePattern;

    @Value("${ksf.jenkins.job.namePattern}")
    private String jobNamePattern;

    @Value("${ksf.jenkins.job.releasePattern}")
    private String jobReleasePattern;

    @Value("${ksf.jenkins.job.featurePattern}")
    private String jobFeaturePattern;

    @Value("${ksf.git.branch.releasePattern}")
    private String gitReleaseBranchPattern;

    @Value("${ksf.git.branch.featurePattern}")
    private String gitFeatureBranchPattern;

    public String getUrl() {
        return removeLastSlash(this.url);
    }

    private String removeLastSlash(String url) {
    	if (url != null) {
    		final int length = url.length();
            if (length > 0 && '/' == url.charAt(length - 1)) {
                url = url.substring(0, length - 1);
            }
    	}
        return url;
    }

    public String getTemplateName() {
        return this.templateName;
    }

    public String getScmUrl() {
        return this.scmUrl;
    }

    public boolean useFolders() {
        return this.useFolders;
    }

    public int getBuilsdPerJobLimit() {
        return this.builsdPerJobLimit;
    }

    public String getBuildScript() {
        return this.buildScript;
    }

    public String getPublishScript() {
        return this.publishScript;
    }

    public String getProjectScmUrlPattern() {
        return this.projectScmUrlPattern;
    }

    /**
     * @return the jobReleasePattern
     */
    public String getJobReleasePattern() {
        return this.jobReleasePattern;
    }

    /**
     * @return the jobFeaturePattern
     */
    public String getJobFeaturePattern() {
        return this.jobFeaturePattern;
    }

    /**
     * @return the gitReleaseBranchPattern
     */
    public String getGitReleaseBranchPattern() {
        return this.gitReleaseBranchPattern;
    }

    /**
     * @return the gitFeatureBranchPattern
     */
    public String getGitFeatureBranchPattern() {
        return this.gitFeatureBranchPattern;
    }

    /**
     * @return the jobNamePattern
     */
    public String getJobNamePattern() {
        return this.jobNamePattern;
    }

    /**
     * @return the jobDisplayNamePattern
     */
    public String getJobDisplayNamePattern() {
        return this.jobDisplayNamePattern;
    }
    
    /**
     * @return the template folder
     */
    public String getTemplateFolder() {
    	return this.templateFolder;
    }

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getApiKey() {
		return apiKey;
	}
	
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public boolean isUseFolders() {
		return useFolders;
	}
	

	public void setUseFolders(boolean useFolders) {
		this.useFolders = useFolders;
	}
	

	public void setUrl(String url) {
		this.url = url;
	}
	

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	

	public void setTemplateFolder(String templateFolder) {
		this.templateFolder = templateFolder;
	}
	

	public void setScmUrl(String scmUrl) {
		this.scmUrl = scmUrl;
	}
	

	public void setBuilsdPerJobLimit(int builsdPerJobLimit) {
		this.builsdPerJobLimit = builsdPerJobLimit;
	}
	

	public void setBuildScript(String buildScript) {
		this.buildScript = buildScript;
	}
	

	public void setPublishScript(String publishScript) {
		this.publishScript = publishScript;
	}
	

	public void setProjectScmUrlPattern(String projectScmUrlPattern) {
		this.projectScmUrlPattern = projectScmUrlPattern;
	}
	

	public void setJobDisplayNamePattern(String jobDisplayNamePattern) {
		this.jobDisplayNamePattern = jobDisplayNamePattern;
	}
	

	public void setJobNamePattern(String jobNamePattern) {
		this.jobNamePattern = jobNamePattern;
	}
	

	public void setJobReleasePattern(String jobReleasePattern) {
		this.jobReleasePattern = jobReleasePattern;
	}
	

	public void setJobFeaturePattern(String jobFeaturePattern) {
		this.jobFeaturePattern = jobFeaturePattern;
	}
	

	public void setGitReleaseBranchPattern(String gitReleaseBranchPattern) {
		this.gitReleaseBranchPattern = gitReleaseBranchPattern;
	}
	
	public void setGitFeatureBranchPattern(String gitFeatureBranchPattern) {
		this.gitFeatureBranchPattern = gitFeatureBranchPattern;
	}	
	
}
