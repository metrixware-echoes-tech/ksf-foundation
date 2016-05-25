package fr.echoes.labs.komea.foundation.plugins.jenkins.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author dcollard
 *
 */
@Service("jenkinsConfiguration")
public class JenkinsConfigurationService {

	@Value("${ksf.jenkins.url}")
	private String url;

	@Value("${ksf.jenkins.templateName}")
	private String templateName;

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

	public String getUrl() {
        if ('/' == this.url.charAt(this.url.length() - 1)) {
            this.url = this.url.substring(0, this.url.length() - 1);
        }
        return this.url;
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
}
