package fr.echoes.labs.ksf.cc.plugins.foreman;

import org.springframework.beans.factory.annotation.Value;

public class ForemanConfigurationBean {

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

    @Value("${ksf.foreman.host.domainId:1}")
    private String domainId;

	@Value("${ksf.foreman.host.rootPassword}")
    private String rootPassword;

    @Value("${ksf.foreman.host.architectureId:1}")
    private String architectureId;

    @Value("${ksf.foreman.puppet.configuration.create.parameters.enabled}")
    private Boolean createParametersEnabled;

    @Value("${ksf.foreman.hostgroup.subnetId:1}")
    private String hostgroupSubnetId;

    @Value("${ksf.foreman.hostgroup.realmId:}")
    private String hostgroupRealmId;

    @Value("${ksf.foreman.hostgroup.operatingsystemId:1}")
    private String hostgroupOperatingsystemId;

    @Value("${ksf.foreman.hostgroup.ptableId:1}")
    private String hostgroupPtableId;

    @Value("${ksf.foreman.hostgroup.mediumId:1}")
    private String hostgroupMediumId;

    @Value("${ksf.foreman.puppetModuleInstallScript:}")
    private String puppetModuleInstallScript;

    @Value("${ksf.foreman.provisionMethod:build}")
    private String provisionMethod;

    @Value("${ksf.foreman.imageId:}")
	private String imageId;

    @Value("${ksf.foreman.unusedIpScript:}")
	private String unusedIpScript;

    public Boolean getCreateParametersEnabled() {
		return this.createParametersEnabled;
	}

	public String getForemanUrl() {
        if (this.url != null && '/' == this.url.charAt(this.url.length() - 1)) {
            this.url = this.url.substring(0, this.url.length() - 1);
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
		return this.smartProxyId;
	}


	public String getComputeResourceId() {
		return this.computeResourceId;
	}


	public String getComputeProfileId() {
		return this.computeProfileId;
	}


	public String getDomainId() {
		return this.domainId;
	}


	public String getRootPassword() {
		return this.rootPassword;
	}


	public String getArchitectureId() {
		return this.architectureId;
	}

	/**
	 * @return the hostgroupSubnetId
	 */
	public String getHostgroupSubnetId() {
		return this.hostgroupSubnetId;
	}

	/**
	 * @return the hostgroupOperatingsystemId
	 */
	public String getHostgroupOperatingsystemId() {
		return this.hostgroupOperatingsystemId;
	}

	/**
	 * @return the hostgroupPtableId
	 */
	public String getHostgroupPtableId() {
		return this.hostgroupPtableId;
	}

	/**
	 * @return the hostgroupMediumId
	 */
	public String getHostgroupMediumId() {
		return this.hostgroupMediumId;
	}

	/**
	 * @return the hostgroupRealmId
	 */
	public String getHostgroupRealmId() {
		return this.hostgroupRealmId;
	}

	/**
	 * @return the puppetModuleInstallScript
	 */
	public String getPuppetModuleInstallScript() {
		return this.puppetModuleInstallScript;
	}

	/**
	 * @return the provisionMethod
	 */
	public String getProvisionMethod() {
		return this.provisionMethod;
	}

	/**
	 * @return the imageId
	 */
	public String getImageId() {
		return this.imageId;
	}

	/**
	 * @return the unusedIpScript
	 */
	public String getUnusedIpScript() {
		return this.unusedIpScript;
	}

	public String getUrl() {
		return url;
	}
	

	public void setUrl(String url) {
		this.url = url;
	}
	

	public String getUsername() {
		return username;
	}
	

	public void setUsername(String username) {
		this.username = username;
	}
	

	public String getPassword() {
		return password;
	}
	

	public void setPassword(String password) {
		this.password = password;
	}
	

	public void setSmartProxyId(String smartProxyId) {
		this.smartProxyId = smartProxyId;
	}
	

	public void setComputeResourceId(String computeResourceId) {
		this.computeResourceId = computeResourceId;
	}
	

	public void setComputeProfileId(String computeProfileId) {
		this.computeProfileId = computeProfileId;
	}
	

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}
	

	public void setRootPassword(String rootPassword) {
		this.rootPassword = rootPassword;
	}
	

	public void setArchitectureId(String architectureId) {
		this.architectureId = architectureId;
	}
	

	public void setCreateParametersEnabled(Boolean createParametersEnabled) {
		this.createParametersEnabled = createParametersEnabled;
	}
	

	public void setHostgroupSubnetId(String hostgroupSubnetId) {
		this.hostgroupSubnetId = hostgroupSubnetId;
	}
	

	public void setHostgroupRealmId(String hostgroupRealmId) {
		this.hostgroupRealmId = hostgroupRealmId;
	}
	

	public void setHostgroupOperatingsystemId(String hostgroupOperatingsystemId) {
		this.hostgroupOperatingsystemId = hostgroupOperatingsystemId;
	}
	

	public void setHostgroupPtableId(String hostgroupPtableId) {
		this.hostgroupPtableId = hostgroupPtableId;
	}
	

	public void setHostgroupMediumId(String hostgroupMediumId) {
		this.hostgroupMediumId = hostgroupMediumId;
	}
	

	public void setPuppetModuleInstallScript(String puppetModuleInstallScript) {
		this.puppetModuleInstallScript = puppetModuleInstallScript;
	}
	

	public void setProvisionMethod(String provisionMethod) {
		this.provisionMethod = provisionMethod;
	}
	

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	

	public void setUnusedIpScript(String unusedIpScript) {
		this.unusedIpScript = unusedIpScript;
	}
	
}
