package fr.echoes.labs.ksf.foreman.actions;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.foreman.api.client.ForemanClient;
import fr.echoes.labs.ksf.foreman.api.model.ForemanHost;
import fr.echoes.labs.ksf.foreman.api.model.ForemanHostGroup;
import fr.echoes.labs.ksf.foreman.api.model.PuppetClass;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameter;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterOverrideValue;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterWrapper;
import fr.echoes.labs.ksf.foreman.api.utils.ScParamsUtils;
import fr.echoes.labs.ksf.foreman.backup.PuppetModulesBackupService;
import fr.echoes.labs.ksf.foreman.backup.SmartClassParameterBackupService;

public class BackupAction implements IAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(BackupAction.class);
	
	final ForemanClient foreman;
	final PuppetModulesBackupService hostPuppetModulesBackupService;
	final SmartClassParameterBackupService smartParamBackupdService;
	
	public BackupAction(final ForemanClient client, final SmartClassParameterBackupService smartParamBackupdService, final PuppetModulesBackupService hostPuppetModulesBackupService) {
		this.foreman = client;
		this.hostPuppetModulesBackupService = hostPuppetModulesBackupService;
		this.smartParamBackupdService = smartParamBackupdService;
	}
	
	@Override
	public void execute() throws IOException {
		
		// retrieve the list of hosts
		LOGGER.info("Retrieving foreman hosts...");
		final List<ForemanHost> hosts = foreman.getHosts();
		
		LOGGER.info("{} hosts found.", hosts.size());
		
		// retrieve the list of host groups
		LOGGER.info("Retrieving foreman host groups...");
		final List<ForemanHostGroup> hostGroups = foreman.getHostGroups();
		
		LOGGER.info("{} host groups found.", hostGroups.size());
		
		// retrieve the list of smart class parameters with override values
		LOGGER.info("Retrieving smart class parameters...");
		List<SmartClassParameter> params = foreman.getSmartClassParametersWithOverrideValues();
		params = foreman.getDetails(params);
		
		LOGGER.info("{} smart class parameters", params.size());
		
		// export global override values to CSV
		final List<SmartClassParameterWrapper> overrideValues = Lists.newArrayList();
		for(final SmartClassParameter param : params) {
			overrideValues.add(new SmartClassParameterWrapper(param));
		}
		smartParamBackupdService.write(overrideValues);

		// export host group override values to CSV
		for (final Entry<String, List<SmartClassParameterWrapper>> entry : ScParamsUtils.groupByHostGroup(params).entrySet()) {
			smartParamBackupdService.writeHostGroupValues(entry.getKey(), entry.getValue());
		}
		
		// export host group classes to CSV
		for (final ForemanHostGroup hostGroup : hostGroups) {
			LOGGER.info("Retrieving puppet classes of host group {}...", hostGroup.getName());
			final List<PuppetClass> puppetClasses = foreman.getPuppetClassesOfHostGroup(hostGroup);
			hostPuppetModulesBackupService.writeHostGroupClasses(hostGroup.getFullName(), puppetClasses);
		}
		
		// export OS override values to CSV
		for (final Entry<String, List<SmartClassParameterWrapper>> entry : ScParamsUtils.groupByOs(params).entrySet()) {
			smartParamBackupdService.writeOsValues(entry.getKey(), entry.getValue());
		}
		
		// export domain override values to CSV
		for (final Entry<String, List<SmartClassParameterWrapper>> entry : ScParamsUtils.groupByDomain(params).entrySet()) {
			smartParamBackupdService.writeDomainValues(entry.getKey(), entry.getValue());
		}
		
		for(final ForemanHost host : hosts) {
			
			// retrieve host's details
			LOGGER.info("Retrieving details of host {}", host.getName());
			final ForemanHost targetHost = foreman.getHost(host.getName());
			
			// export host's puppet classes list to CSV
			final List<PuppetClass> puppetClasses = foreman.getPuppetClassesOfHost(targetHost);
			hostPuppetModulesBackupService.writeHostClasses(targetHost, puppetClasses);
			
			final List<SmartClassParameterWrapper> values = Lists.newArrayList();
			
			for(final SmartClassParameter param : params) {
				final SmartClassParameterOverrideValue value = ScParamsUtils.getOverrideValueForHost(param, host.getName());
				if (value != null) {
					values.add(new SmartClassParameterWrapper(param, value));
				}
			}
			
			// export smart class parameters to CSV
			smartParamBackupdService.write(targetHost, values);
		}
		
	}

}
