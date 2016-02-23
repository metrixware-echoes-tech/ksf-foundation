package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author dcollard
 *
 */
@Service("redmineConfiguration")
public class RedmineConfigurationService {

    @Value("${ksf.redmine.url}")
    private String url;

    @Value("${ksf.redmine.apiAccessKey}")
    private String appiAccessKey;

	public String getUrl() {
        if ('/' == this.url.charAt(this.url.length() - 1)) {
            this.url = this.url.substring(0, this.url.length() - 1);
        }
        return this.url;
	}

	public String getApiAccessKey() {
		return this.appiAccessKey;
	}


}
