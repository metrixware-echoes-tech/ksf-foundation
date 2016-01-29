package fr.echoes.lab.foremanclient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import fr.echoes.lab.foremanapi.IForemanApi;
import fr.echoes.lab.foremanapi.model.Host;
import fr.echoes.lab.ksf.cc.plugins.foreman.model.ForemanHostDescriptor;

public interface IForemanService {

	public void createProject(IForemanApi api, String projectName, String userId)
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException;

	public void deleteProject(IForemanApi api, String projectName)
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException;

	public Host createHost(IForemanApi api, ForemanHostDescriptor parameterObject)
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException;

	public void importPuppetClasses(IForemanApi api, String smartProxyId)
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException;

	public String getModulesPuppetClassParameters(IForemanApi api, String environmentName,
			boolean allParameters) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
					JsonGenerationException, JsonMappingException, IOException;

	public boolean hostGroupExists(IForemanApi api, String hostGroupName);

	public boolean hostExists(IForemanApi api, String hostName);

	public void updateValue(IForemanApi api, String smartClassParamId, String hostName, Object value,
			Boolean usePuppetDefault);

}