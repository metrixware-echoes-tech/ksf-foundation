package fr.echoes.labs.ksf.foreman.api.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

import fr.echoes.labs.ksf.foreman.api.model.ForemanHostGroup;
import fr.echoes.labs.ksf.foreman.api.model.PuppetClass;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameter;
import fr.echoes.labs.ksf.foreman.api.model.SmartVariable;
import fr.echoes.labs.ksf.foreman.api.utils.PuppetClassUtils;
import fr.echoes.labs.ksf.foreman.api.utils.ScParamsUtils;

public class ForemanClientProxy extends ForemanClient {

	private Map<Integer, SmartClassParameter> smartClassParameters = Maps.newHashMap();
	private Map<Integer, PuppetClass> puppetClasses = Maps.newHashMap();
	private Map<String, ForemanHostGroup> hostGroups = Maps.newHashMap();
	private Map<String, SmartVariable> smartVariables = Maps.newHashMap();
	
	public ForemanClientProxy(final String url, final String username, final String password) throws MalformedURLException {
		super(url, username, password);
	}
	
	@Override
	public SmartClassParameter getSmartClassParameter(final Integer id) throws IOException {
		
		if (!this.smartClassParameters.containsKey(id)) {
			final SmartClassParameter param = super.getSmartClassParameter(id);
			this.smartClassParameters.put(id, param);
		}
		
		return this.smartClassParameters.get(id);
	}
	
	@Override
	public PuppetClass findPuppetClass(final String moduleName, final String className) throws IOException {
		
		PuppetClass puppetClass = PuppetClassUtils.findPuppetClass(this.puppetClasses.values(), className, moduleName);
		
		if (puppetClass == null) {
			puppetClass = super.findPuppetClass(moduleName, className);
			if (puppetClass != null) {
				this.puppetClasses.put(puppetClass.getId(), puppetClass);
			}
		}
		
		return puppetClass;
	}
	
	@Override
	public PuppetClass getPuppetClass(final Integer id) throws IOException {
		
		if (!this.puppetClasses.containsKey(id)) {
			final PuppetClass param = super.getPuppetClass(id);
			this.puppetClasses.put(id, param);
		}
		
		return this.puppetClasses.get(id);
	}
	
	@Override
	public SmartClassParameter getSmartClassParameterByPuppetClass(final Integer puppetClassId, final String parameter) throws IOException {
		
		SmartClassParameter scParam = ScParamsUtils.findByPuppetClassId(this.smartClassParameters.values(), puppetClassId, parameter);
		
		if (scParam == null) {
			scParam = super.getSmartClassParameterByPuppetClass(puppetClassId, parameter);
			if (scParam != null) {
				this.smartClassParameters.put(scParam.getId(), scParam);
			}
		}
		
		return scParam;
	}
	
	@Override
	public ForemanHostGroup getHostGroup(final String hostGroupName) throws IOException {
		
		if (!this.hostGroups.containsKey(hostGroupName)) {
			final ForemanHostGroup hostGroup = super.getHostGroup(hostGroupName);
			this.hostGroups.put(hostGroupName, hostGroup);
		}

		return this.hostGroups.get(hostGroupName);
	}
	
	@Override
	public SmartVariable getSmartVariableByName(final String variable) throws IOException {
		
		if (!this.smartVariables.containsKey(variable)) {
			final SmartVariable smartVariable = super.getSmartVariableByName(variable);
			this.smartVariables.put(variable, smartVariable);
		}
		
		return this.smartVariables.get(variable);
	}
	
	@Override
	public SmartVariable getSmartVariable(final Integer id) throws IOException {
		
		// look for the smart variable in the cache
		final Collection<SmartVariable> matches = Collections2.filter(this.smartVariables.values(), new Predicate<SmartVariable>() {
			@Override
			public boolean apply(final SmartVariable var) {
				return id.equals(var.getId());
			}
		});
		
		if (!matches.isEmpty()) {
			return matches.iterator().next();
		}
		
		// look for the smart variable through Foreman API
		final SmartVariable smartVariable = super.getSmartVariable(id);
		
		if (smartVariable != null) {
			// store the smart variable in the cache
			this.smartVariables.put(smartVariable.getVariable(), smartVariable);
		}
		
		return smartVariable;
	}
	
}
