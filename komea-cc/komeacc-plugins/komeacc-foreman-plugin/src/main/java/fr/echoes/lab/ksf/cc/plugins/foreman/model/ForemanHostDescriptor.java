package fr.echoes.lab.ksf.cc.plugins.foreman.model;

public class ForemanHostDescriptor {
	
	private String hostName;
	private String computeResourceId;
	private String computeProfileId;
	private String hostGroupName;
	private String environmentName;
	private String operatingSystemId;
	private String architectureId;
	private String puppetConfiguration;
	private String domainId;
	private String rootPassword;

	public ForemanHostDescriptor() {
		
	}
	
	public ForemanHostDescriptor(String hostName, String computeResourceId, String computeProfileId,
			String hostGroupName, String environmentName, String operatingSystemId, String architectureId,
			String puppetConfiguration, String domainId, String rootPassword) {
		this.hostName = hostName;
		this.computeResourceId = computeResourceId;
		this.computeProfileId = computeProfileId;
		this.hostGroupName = hostGroupName;
		this.environmentName = environmentName;
		this.operatingSystemId = operatingSystemId;
		this.architectureId = architectureId;
		this.puppetConfiguration = puppetConfiguration;
		this.domainId = domainId;
		this.rootPassword = rootPassword;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getComputeResourceId() {
		return computeResourceId;
	}

	public void setComputeResourceId(String computeResourceId) {
		this.computeResourceId = computeResourceId;
	}

	public String getComputeProfileId() {
		return computeProfileId;
	}

	public void setComputeProfileId(String computeProfileId) {
		this.computeProfileId = computeProfileId;
	}

	public String getHostGroupName() {
		return hostGroupName;
	}

	public void setHostGroupName(String hostGroupName) {
		this.hostGroupName = hostGroupName;
	}

	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	public String getOperatingSystemId() {
		return operatingSystemId;
	}

	public void setOperatingSystemId(String operatingSystemId) {
		this.operatingSystemId = operatingSystemId;
	}

	public String getArchitectureId() {
		return architectureId;
	}

	public void setArchitectureId(String architectureId) {
		this.architectureId = architectureId;
	}

	public String getPuppetConfiguration() {
		return puppetConfiguration;
	}

	public void setPuppetConfiguration(String puppetConfiguration) {
		this.puppetConfiguration = puppetConfiguration;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getRootPassword() {
		return rootPassword;
	}

	public void setRootPassword(String rootPassword) {
		this.rootPassword = rootPassword;
	}
}