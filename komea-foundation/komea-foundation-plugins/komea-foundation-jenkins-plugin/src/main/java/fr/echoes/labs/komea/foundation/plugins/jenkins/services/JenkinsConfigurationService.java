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
}
