package fr.echoes.labs.ksf.cc.plugins.foreman.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("foremanConfiguration")
public class ForemanConfigurationService {

    @Value("${ksf.foreman.url}")
    private String url;

    @Value("${ksf.foreman.username}")
    private String username;

    @Value("${ksf.foreman.password}")
    private String password;
    
    @Value("${ksf.foreman.host.smartProxyId}")
    private String smartProxyId;

    @Value("${ksf.foreman.host.computeResourceId}")
    private String computeResourceId;

    @Value("${ksf.foreman.host.computeProfileId}")
    private String computeProfileId;

    @Value("${ksf.puppet.modulepath}")
    private String puppetModulePath;

    @Value("${ksf.foreman.host.domainId}")
    private String domainId;	

	@Value("${ksf.foreman.host.rootPassword}")
    private String rootPassword;

    @Value("${ksf.foreman.host.architectureId}")
    private String architectureId;
    
    @Value("${ksf.foreman.puppet.configuration.create.parameters.enabled}")
    private Boolean createParametersEnabled;

    public Boolean getCreateParametersEnabled() {
		return createParametersEnabled;
	}
	
	public String getForemanUrl() {
        if ('/' == url.charAt(url.length() - 1)) {
            url = url.substring(0, url.length() - 1);
        }
        return this.url;
    }

    public String getForemanUsername() {
        return this.username;
    }

    public String getForemanPassword() {
        return this.password;
    }
    
    public String getSmartProxyId() {
		return smartProxyId;
	}
	

	public String getComputeResourceId() {
		return computeResourceId;
	}
	

	public String getComputeProfileId() {
		return computeProfileId;
	}
	

	public String getPuppetModulePath() {
		return puppetModulePath;
	}
	

	public String getDomainId() {
		return domainId;
	}
	

	public String getRootPassword() {
		return rootPassword;
	}
	

	public String getArchitectureId() {
		return architectureId;
	}

}
