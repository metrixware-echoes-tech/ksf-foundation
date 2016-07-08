package fr.echoes.labs.ksf.cc.plugins.foreman.model;

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
	private String provisionMethod;
	private String imageId;

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
		return this.hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getComputeResourceId() {
		return this.computeResourceId;
	}

	public void setComputeResourceId(String computeResourceId) {
		this.computeResourceId = computeResourceId;
	}

	public String getComputeProfileId() {
		return this.computeProfileId;
	}

	public void setComputeProfileId(String computeProfileId) {
		this.computeProfileId = computeProfileId;
	}

	public String getHostGroupName() {
		return this.hostGroupName;
	}

	public void setHostGroupName(String hostGroupName) {
		this.hostGroupName = hostGroupName;
	}

	public String getEnvironmentName() {
		return this.environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	public String getOperatingSystemId() {
		return this.operatingSystemId;
	}

	public void setOperatingSystemId(String operatingSystemId) {
		this.operatingSystemId = operatingSystemId;
	}

	public String getArchitectureId() {
		return this.architectureId;
	}

	public void setArchitectureId(String architectureId) {
		this.architectureId = architectureId;
	}

	public String getPuppetConfiguration() {
		return this.puppetConfiguration;
	}

	public void setPuppetConfiguration(String puppetConfiguration) {
		this.puppetConfiguration = puppetConfiguration;
	}

	public String getDomainId() {
		return this.domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getRootPassword() {
		return this.rootPassword;
	}

	public void setRootPassword(String rootPassword) {
		this.rootPassword = rootPassword;
	}

	public String getProvisionMethod() {
		return this.provisionMethod;
	}

	public void setProvisionMethod(String provisionMethod) {
		this.provisionMethod = provisionMethod;
	}

	public String getImageId() {
		return this.imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
}