package fr.echoes.labs.ksf.foreman.api.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import com.google.common.collect.Maps;

import fr.echoes.labs.ksf.foreman.api.model.ForemanHostGroup;
import fr.echoes.labs.ksf.foreman.api.model.PuppetClass;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameter;
import fr.echoes.labs.ksf.foreman.api.utils.PuppetClassUtils;
import fr.echoes.labs.ksf.foreman.api.utils.ScParamsUtils;

public class ForemanClientProxy extends ForemanClient {

	private Map<Integer, SmartClassParameter> smartClassParameters = Maps.newHashMap();
	private Map<Integer, PuppetClass> puppetClasses = Maps.newHashMap();
	private Map<String, ForemanHostGroup> hostGroups = Maps.newHashMap();
	
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
	
}
