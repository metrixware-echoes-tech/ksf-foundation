package fr.echoes.labs.ksf.foreman.actions;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.echoes.labs.ksf.foreman.api.client.ForemanClient;
import fr.echoes.labs.ksf.foreman.api.model.ForemanHost;
import fr.echoes.labs.ksf.foreman.api.model.ForemanHostGroup;
import fr.echoes.labs.ksf.foreman.api.model.PuppetClass;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameter;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterOverrideValue;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterWrapper;
import fr.echoes.labs.ksf.foreman.api.utils.ForemanEntities;
import fr.echoes.labs.ksf.foreman.backup.BackupStorage;
import fr.echoes.labs.ksf.foreman.backup.PuppetModulesBackupService;
import fr.echoes.labs.ksf.foreman.backup.SmartClassParameterBackupService;
import fr.echoes.labs.ksf.foreman.exceptions.HostGroupNotFoundException;
import fr.echoes.labs.ksf.foreman.exceptions.HostNotFoundException;

public class InstallActionTest {

	private ForemanClient client;
	private SmartClassParameterBackupService scParamBackupService;
	private PuppetModulesBackupService modulesBackupService;
	private BackupStorage storage;
	private InstallAction action;
	
	private final PuppetClass puppetClass1 = new PuppetClass(1, "puppetClass1", "puppetModule1");
	private final PuppetClass puppetClass2 = new PuppetClass(2, "puppetClass2", "puppetModule2");
	
	private ArgumentCaptor<SmartClassParameter> scParamCaptor = ArgumentCaptor.forClass(SmartClassParameter.class);
	private ArgumentCaptor<SmartClassParameterOverrideValue> overrideValueCaptor = ArgumentCaptor.forClass(SmartClassParameterOverrideValue.class);
	
	@Before
	public void setup() throws IOException {
		
		this.client = Mockito.mock(ForemanClient.class);
		this.scParamBackupService = Mockito.mock(SmartClassParameterBackupService.class);
		this.modulesBackupService = Mockito.mock(PuppetModulesBackupService.class);
		this.storage = Mockito.mock(BackupStorage.class);
		this.action = new InstallAction(this.client, this.scParamBackupService, this.modulesBackupService, this.storage);

	}
	
	@Test
	public void testDoNothing() throws IOException {
		
		action.execute();
	}
	
	@Test
	public void testConfigureOverrideValues() throws IOException {
		
		// given
		final SmartClassParameterWrapper param1 = new SmartClassParameterWrapper();
		param1.setParameter("param1");
		param1.setPuppetClass(puppetClass1.getName());
		param1.setPuppetModule(puppetClass1.getModuleName());
		param1.setUsePuppetDefault(false);
		param1.setValue("blblbl");
		param1.setType("hash");
		
		final SmartClassParameterWrapper param2 = new SmartClassParameterWrapper();
		param2.setParameter("param2");
		param2.setPuppetClass(puppetClass2.getName());
		param2.setPuppetModule(puppetClass2.getModuleName());
		param2.setUsePuppetDefault(true);
		param2.setValue("nope");
		param2.setType("Integer");
		
		final SmartClassParameter scParam1 = new SmartClassParameter();
		scParam1.setId(1);
		scParam1.setParameter(param1.getParameter());
		scParam1.setPuppetClass(puppetClass1);
		scParam1.setUsePuppetDefault(true);
		
		final SmartClassParameter scParam2 = new SmartClassParameter();
		scParam2.setId(2);
		scParam2.setParameter(param2.getParameter());
		scParam2.setPuppetClass(puppetClass2);
		scParam2.setUsePuppetDefault(true);
		
		final List<SmartClassParameterWrapper> params = Lists.newArrayList(param1, param2);
		
		// mock
		Mockito.when(scParamBackupService.read()).thenReturn(params);
		
		Mockito.when(client.findPuppetClass(param1.getPuppetModule(), param1.getPuppetClass())).thenReturn(puppetClass1);
		Mockito.when(client.getSmartClassParameterByPuppetClass(puppetClass1.getId(), param1.getParameter())).thenReturn(scParam1);
		
		Mockito.when(client.findPuppetClass(param2.getPuppetModule(), param2.getPuppetClass())).thenReturn(puppetClass2);
		Mockito.when(client.getSmartClassParameterByPuppetClass(puppetClass2.getId(), param2.getParameter())).thenReturn(scParam2);
		
		// when
		action.execute();
		
		// then
		Mockito.verify(client, Mockito.times(2)).updateSmartClassParameter(this.scParamCaptor.capture());
		
		// then
		final List<SmartClassParameter> valuesSaved = this.scParamCaptor.getAllValues();
		
		// then
		Assert.assertEquals(scParam1.getId(), valuesSaved.get(0).getId());
		Assert.assertEquals(scParam1.getParameter(), valuesSaved.get(0).getParameter());
		Assert.assertEquals(puppetClass1, valuesSaved.get(0).getPuppetClass());
		Assert.assertEquals(true, valuesSaved.get(0).isOverride());
		Assert.assertEquals(param1.getUsePuppetDefault(), valuesSaved.get(0).isUsePuppetDefault());
		Assert.assertEquals(param1.getValue(), valuesSaved.get(0).getDefaultValue());
		Assert.assertEquals(param1.getType(), valuesSaved.get(0).getType());
		
		// then
		Assert.assertEquals(scParam2.getId(), valuesSaved.get(1).getId());
		Assert.assertEquals(scParam2.getParameter(), valuesSaved.get(1).getParameter());
		Assert.assertEquals(puppetClass2, valuesSaved.get(1).getPuppetClass());
		Assert.assertEquals(true, valuesSaved.get(1).isOverride());
		Assert.assertEquals(param2.getUsePuppetDefault(), valuesSaved.get(1).isUsePuppetDefault());
		Assert.assertEquals(null, valuesSaved.get(1).getDefaultValue());
		Assert.assertEquals(param2.getType(), valuesSaved.get(1).getType());
		
	}
	
	@Test
	public void testActivatePuppetClassesForHostGroups() throws IOException {
		
		// given
		final ForemanHostGroup group1 = new ForemanHostGroup(1, "group1");
		
		final PuppetClass puppetClass3 = new PuppetClass(3, "puppetClass3", "puppetModule3");
		puppetClass3.setHostGroups(Lists.newArrayList(group1));
		
		final Map<String, List<PuppetClass>> hostGroupClasses = Maps.newHashMap();
		hostGroupClasses.put(group1.getName(), Lists.newArrayList(puppetClass1, puppetClass2, puppetClass3));
		
		// mock
		Mockito.when(this.modulesBackupService.readHostGroups()).thenReturn(hostGroupClasses);
		Mockito.when(client.getHostGroup(group1.getName())).thenReturn(group1);
		Mockito.when(client.findPuppetClass(puppetClass1.getModuleName(), puppetClass1.getName())).thenReturn(puppetClass1);
		Mockito.when(client.findPuppetClass(puppetClass2.getModuleName(), puppetClass2.getName())).thenReturn(puppetClass2);
		Mockito.when(client.findPuppetClass(puppetClass3.getModuleName(), puppetClass3.getName())).thenReturn(puppetClass3);
		
		// when
		action.execute();
		
		// then
		Mockito.verify(client, Mockito.times(1)).addPuppetClassToHostGroup(puppetClass1, group1);
		Mockito.verify(client, Mockito.times(1)).addPuppetClassToHostGroup(puppetClass2, group1);
		Mockito.verify(client, Mockito.times(0)).addPuppetClassToHostGroup(puppetClass3, group1);
		
	}
	
	@Test
	public void testImportHostGroupsOverrideValues() throws IOException {

		// given : a host group
		final ForemanHostGroup group1 = new ForemanHostGroup(1, "group1");
		final String matcherForGroup1 = ForemanEntities.buildMatcher(ForemanEntities.TYPE_HOSTGROUP, group1.getName());
		
		// given : 2 smart class parameters override values
		final SmartClassParameterWrapper param1 = new SmartClassParameterWrapper("param1", "Integer", puppetClass1, "35", false);
		final SmartClassParameterWrapper param2 = new SmartClassParameterWrapper("param2", "Integer", puppetClass2, "56", false);
		
		final SmartClassParameterOverrideValue overrideValue = new SmartClassParameterOverrideValue();
		overrideValue.setId(1);
		overrideValue.setMatch(matcherForGroup1);
		overrideValue.setValue("a value to override!");
		
		final SmartClassParameter scParam1 = new SmartClassParameter();
		scParam1.setId(1);
		scParam1.setParameter(param1.getParameter());
		scParam1.setPuppetClass(puppetClass1);
		scParam1.setUsePuppetDefault(true);
		scParam1.setOverrideValues(Lists.newArrayList(overrideValue));
		
		final SmartClassParameter scParam2 = new SmartClassParameter();
		scParam2.setId(2);
		scParam2.setParameter(param2.getParameter());
		scParam2.setPuppetClass(puppetClass2);
		scParam2.setUsePuppetDefault(true);
		
		final Map<String, List<SmartClassParameterWrapper>> hostGroupsValues = Maps.newHashMap();
		hostGroupsValues.put(group1.getName(), Lists.newArrayList(param1, param2));
		
		// mock
		Mockito.when(this.scParamBackupService.readHostGroupValues()).thenReturn(hostGroupsValues);
		Mockito.when(client.getHostGroup(group1.getName())).thenReturn(group1);
		
		Mockito.when(client.findPuppetClass(param1.getPuppetModule(), param1.getPuppetClass())).thenReturn(puppetClass1);
		Mockito.when(client.getSmartClassParameterByPuppetClass(puppetClass1.getId(), param1.getParameter())).thenReturn(scParam1);
		
		Mockito.when(client.findPuppetClass(param2.getPuppetModule(), param2.getPuppetClass())).thenReturn(puppetClass2);
		Mockito.when(client.getSmartClassParameterByPuppetClass(puppetClass2.getId(), param2.getParameter())).thenReturn(scParam2);
		
		// when
		action.execute();
		
		// then
		Mockito.verify(client, Mockito.times(1)).updateSmartClassParameterOverrideValue(Matchers.eq(scParam1.getId()), this.overrideValueCaptor.capture());
		Mockito.verify(client, Mockito.times(1)).createSmartClassParameterOverrideValue(Matchers.eq(scParam2.getId()), this.overrideValueCaptor.capture());	
		final List<SmartClassParameterOverrideValue> overrideValuesSaved = this.overrideValueCaptor.getAllValues();
		
		// then
		Assert.assertEquals(overrideValue.getId(), overrideValuesSaved.get(0).getId());
		Assert.assertEquals(matcherForGroup1, overrideValuesSaved.get(0).getMatch());
		Assert.assertEquals(param1.getValue(), overrideValuesSaved.get(0).getValue());
		Assert.assertEquals(param1.getUsePuppetDefault(), overrideValuesSaved.get(0).getUsePuppetDefault());
		
		// then
		Assert.assertEquals(null, overrideValuesSaved.get(1).getId());
		Assert.assertEquals(matcherForGroup1, overrideValuesSaved.get(1).getMatch());
		Assert.assertEquals(param2.getValue(), overrideValuesSaved.get(1).getValue());
		Assert.assertEquals(param2.getUsePuppetDefault(), overrideValuesSaved.get(1).getUsePuppetDefault());
		
	}
	
	@Test(expected = HostGroupNotFoundException.class)
	public void testHostGroupNotFoundWhileAddingPuppetClasses() throws IOException {
		
		// given
		final Map<String, List<PuppetClass>> hostGroupsValues = Maps.newHashMap();
		hostGroupsValues.put("deadGroup", null);
		
		// mock
		Mockito.when(this.modulesBackupService.readHostGroups()).thenReturn(hostGroupsValues);
		
		// when
		action.execute();
		
	}
	
	@Test(expected = HostGroupNotFoundException.class)
	public void testHostGroupNotFoundWhileOverridingValues() throws IOException {
		
		// given
		final Map<String, List<SmartClassParameterWrapper>> hostGroupsValues = Maps.newHashMap();
		hostGroupsValues.put("deadGroup", null);
		
		// mock
		Mockito.when(this.scParamBackupService.readHostGroupValues()).thenReturn(hostGroupsValues);
		
		// when
		action.execute();
	}
	
	@Test
	public void testAddPuppetClassesToHosts() throws IOException {
		
		// given
		final PuppetClass puppetClass3 = new PuppetClass(3, "puppetClass3", "puppetModule3");
		
		final ForemanHost host1 = new ForemanHost("1", "foreman.ksf.local");
		host1.setPuppetclasses(Lists.newArrayList(puppetClass3));
		
		// mock
		Mockito.when(storage.readHosts()).thenReturn(Lists.newArrayList(host1.getName()));
		Mockito.when(client.getHost(host1.getName())).thenReturn(host1);
		Mockito.when(this.modulesBackupService.readHostClasses(host1)).thenReturn(Lists.newArrayList(puppetClass1, puppetClass2, puppetClass3));
		Mockito.when(client.findPuppetClass(puppetClass1.getModuleName(), puppetClass1.getName())).thenReturn(puppetClass1);
		Mockito.when(client.findPuppetClass(puppetClass2.getModuleName(), puppetClass2.getName())).thenReturn(puppetClass2);
		Mockito.when(client.findPuppetClass(puppetClass3.getModuleName(), puppetClass3.getName())).thenReturn(puppetClass3);
		
		// when
		action.execute();
		
		// then
		Mockito.verify(client, Mockito.times(1)).addPuppetClassToHost(puppetClass1, host1);
		Mockito.verify(client, Mockito.times(1)).addPuppetClassToHost(puppetClass2, host1);
		Mockito.verify(client, Mockito.times(0)).addPuppetClassToHost(puppetClass3, host1);
		
	}
	
	@Test
	public void testImportOverrideValuesInHosts() throws IOException {
		
		// given
		final ForemanHost host1 = new ForemanHost("1", "foreman.ksf.local");
		final String matcherForHost1 = ForemanEntities.buildMatcher(ForemanEntities.TYPE_FQDN, host1.getName());

		final SmartClassParameterWrapper param1 = new SmartClassParameterWrapper("param1", "Integer", puppetClass1, "35", false);
		final SmartClassParameterWrapper param2 = new SmartClassParameterWrapper("param2", "Integer", puppetClass2, "56", false);
		
		final SmartClassParameterOverrideValue overrideValue = new SmartClassParameterOverrideValue();
		overrideValue.setId(1);
		overrideValue.setMatch(matcherForHost1);
		overrideValue.setValue("a value to override!");
		
		final SmartClassParameter scParam1 = new SmartClassParameter();
		scParam1.setId(1);
		scParam1.setParameter(param1.getParameter());
		scParam1.setPuppetClass(puppetClass1);
		scParam1.setUsePuppetDefault(true);
		scParam1.setOverrideValues(Lists.newArrayList(overrideValue));
		
		final SmartClassParameter scParam2 = new SmartClassParameter();
		scParam2.setId(2);
		scParam2.setParameter(param2.getParameter());
		scParam2.setPuppetClass(puppetClass2);
		scParam2.setUsePuppetDefault(true);
		
		// mock
		Mockito.when(storage.readHosts()).thenReturn(Lists.newArrayList(host1.getName()));
		Mockito.when(client.getHost(host1.getName())).thenReturn(host1);
		Mockito.when(this.scParamBackupService.read(host1)).thenReturn(Lists.newArrayList(param1, param2));
		Mockito.when(client.findPuppetClass(puppetClass1.getModuleName(), puppetClass1.getName())).thenReturn(puppetClass1);
		Mockito.when(client.findPuppetClass(puppetClass2.getModuleName(), puppetClass2.getName())).thenReturn(puppetClass2);
		Mockito.when(client.getSmartClassParameterByPuppetClass(puppetClass1.getId(), param1.getParameter())).thenReturn(scParam1);
		Mockito.when(client.getSmartClassParameterByPuppetClass(puppetClass2.getId(), param2.getParameter())).thenReturn(scParam2);
		
		// when
		action.execute();
		
		// then
		Mockito.verify(client, Mockito.times(1)).updateSmartClassParameterOverrideValue(Matchers.eq(scParam1.getId()), this.overrideValueCaptor.capture());
		Mockito.verify(client, Mockito.times(1)).createSmartClassParameterOverrideValue(Matchers.eq(scParam2.getId()), this.overrideValueCaptor.capture());	
		final List<SmartClassParameterOverrideValue> overrideValuesSaved = this.overrideValueCaptor.getAllValues();
		
		// then
		Assert.assertEquals(overrideValue.getId(), overrideValuesSaved.get(0).getId());
		Assert.assertEquals(matcherForHost1, overrideValuesSaved.get(0).getMatch());
		Assert.assertEquals(param1.getValue(), overrideValuesSaved.get(0).getValue());
		Assert.assertEquals(param1.getUsePuppetDefault(), overrideValuesSaved.get(0).getUsePuppetDefault());
		
		// then
		Assert.assertEquals(null, overrideValuesSaved.get(1).getId());
		Assert.assertEquals(matcherForHost1, overrideValuesSaved.get(1).getMatch());
		Assert.assertEquals(param2.getValue(), overrideValuesSaved.get(1).getValue());
		Assert.assertEquals(param2.getUsePuppetDefault(), overrideValuesSaved.get(1).getUsePuppetDefault());
		
	}
	
	@Test(expected = HostNotFoundException.class)
	public void testHostNotFound() throws IOException {
		
		// mock
		Mockito.when(storage.readHosts()).thenReturn(Lists.newArrayList("hostDead"));
		Mockito.when(client.getHost("hostDead")).thenReturn(null);
		
		// when
		action.execute();
		
	}
	
}
