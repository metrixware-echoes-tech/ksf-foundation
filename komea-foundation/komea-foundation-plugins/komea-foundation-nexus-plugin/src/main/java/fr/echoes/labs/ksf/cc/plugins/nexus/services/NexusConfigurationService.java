package fr.echoes.labs.ksf.cc.plugins.nexus.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author dcollard
 *
 */
@Service("nexusConfiguration")
public class NexusConfigurationService {

    @Value("${ksf.nexus.url}")
    private String url;

	public String getUrl() {
        if ('/' == this.url.charAt(this.url.length() - 1)) {
            this.url = this.url.substring(0, this.url.length() - 1);
        }
        return this.url;
	}

}
