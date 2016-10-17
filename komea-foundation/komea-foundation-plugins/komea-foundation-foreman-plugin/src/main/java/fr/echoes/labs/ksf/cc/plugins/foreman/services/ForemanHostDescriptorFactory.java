package fr.echoes.labs.ksf.cc.plugins.foreman.services;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tocea.corolla.products.domain.Project;

import fr.echoes.labs.ksf.cc.plugins.foreman.model.ForemanEnvironnment;
import fr.echoes.labs.ksf.cc.plugins.foreman.model.ForemanHostDescriptor;
import fr.echoes.labs.ksf.cc.plugins.foreman.model.ForemanTarget;

@Service
public class ForemanHostDescriptorFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ForemanHostDescriptorFactory.class);

	private static final String PROVISION_METHOD_IMAGE = "image";
	
	@Autowired
	private ForemanConfigurationService configurationService;

	/**
	 * Creates a HostDescriptor object that can be used to instantiate a Host in Foreman.
	 * @param project the KSF project associated to the host
	 * @param target the target associated to the host
	 * @param hostName the hostName
	 * @param hostPass the password that will be set during the VM creation (a default password is used if empty)
	 * @return a HostDescriptor instance
	 */
	public ForemanHostDescriptor createHostDescriptor(final Project project, final ForemanTarget target, final String hostName, final String hostPass) {

		final ForemanEnvironnment environment = target.getEnvironment();

		final String hostGroupName = project.getName();
		final String environmentName = environment.getName();
		final String operatingSystemId = target.getOperationSystemId();
		final String puppetConfiguration = target.getPuppetConfiguration();
		final String imageId = target.getImageUuid();
		final Integer architectureId = target.getArchitectureId();
		final String provisionMethod = PROVISION_METHOD_IMAGE;
		
		String computeProfileId = target.getComputeProfile();

		if (StringUtils.isEmpty(computeProfileId)) {
			computeProfileId = this.configurationService.getComputeProfileId();
		}

		String passwordVm = this.configurationService.getRootPassword();
	    if (StringUtils.isNotEmpty(hostPass)) {
	    	passwordVm = hostPass;
	    }

		LOGGER.info("[foreman] hostName: {}", hostName);
		LOGGER.info("[foreman] computeResourceId: {}", this.configurationService.getComputeResourceId());
		LOGGER.info("[foreman] computeProfileId: {}", computeProfileId);
		LOGGER.info("[foreman] hostGroupName: {}", hostGroupName);
		LOGGER.info("[foreman] environmentName: {}", environmentName);
		LOGGER.info("[foreman] operatingSystemId: {}", operatingSystemId);
		LOGGER.info("[foreman] architectureId: {}", architectureId);
		LOGGER.info("[foreman] puppetConfiguration: {}", puppetConfiguration);
		LOGGER.info("[foreman] domainId: {}", this.configurationService.getDomainId());
		LOGGER.info("[foreman] imageId: {}", imageId);

    	final ForemanHostDescriptor hostDescriptor = new ForemanHostDescriptor();
    	hostDescriptor.setHostName(hostName);
    	hostDescriptor.setComputeResourceId(this.configurationService.getComputeResourceId());
    	hostDescriptor.setComputeProfileId(computeProfileId);
    	hostDescriptor.setHostGroupName(hostGroupName);
    	hostDescriptor.setEnvironmentName(environmentName);
    	hostDescriptor.setOperatingSystemId(operatingSystemId);
    	hostDescriptor.setArchitectureId(Integer.toString(architectureId));
    	hostDescriptor.setPuppetConfiguration(puppetConfiguration);
    	hostDescriptor.setDomainId(this.configurationService.getDomainId());
    	hostDescriptor.setRootPassword(passwordVm);
    	hostDescriptor.setProvisionMethod(provisionMethod);
    	hostDescriptor.setImageId(imageId);

    	return hostDescriptor;
	}

}
