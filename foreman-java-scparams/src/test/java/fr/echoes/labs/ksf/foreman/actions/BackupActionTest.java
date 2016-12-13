package fr.echoes.labs.ksf.foreman.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.foreman.api.client.ForemanClient;
import fr.echoes.labs.ksf.foreman.api.model.ForemanHost;
import fr.echoes.labs.ksf.foreman.api.model.ForemanHostGroup;
import fr.echoes.labs.ksf.foreman.api.model.PuppetClass;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameter;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterOverrideValue;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterWrapper;
import fr.echoes.labs.ksf.foreman.api.utils.ForemanEntities;
import fr.echoes.labs.ksf.foreman.backup.PuppetModulesBackupService;
import fr.echoes.labs.ksf.foreman.backup.SmartClassParameterBackupService;
import fr.echoes.labs.ksf.foreman.backup.SmartVariableBackupService;

public class BackupActionTest {

	private BackupAction action;
	
	@Mock
	private ForemanClient client;
	
	@Mock
	private SmartClassParameterBackupService scParamBackupService;
	
	@Mock
	private PuppetModulesBackupService modulesBackupService;
	
	@Mock
	private SmartVariableBackupService smartVariableBackupService;
	
	@Captor
	private ArgumentCaptor<ArrayList<SmartClassParameterWrapper>> paramCaptor;
	
	private final PuppetClass puppetClass1 = new PuppetClass(1, "class1", "module1");
	private final PuppetClass puppetClass2 = new PuppetClass(2, "class2", "module2");
	
	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		this.action = new BackupAction(this.client, this.scParamBackupService, this.modulesBackupService, this.smartVariableBackupService);
		
	}
	
	@Test
	public void testExportGlobalOverrideValues() throws IOException {
		
		// given
		final List<SmartClassParameterOverrideValue> overrideValues = Lists.newArrayList();
		
		final SmartClassParameter scParam1 = new SmartClassParameter();
		scParam1.setParameter("param1");
		scParam1.setPuppetClass(puppetClass1);
		scParam1.setDefaultValue("value1");
		scParam1.setOverrideValues(overrideValues);
		
		final SmartClassParameter scParam2 = new SmartClassParameter();
		scParam2.setParameter("param2");
		scParam2.setPuppetClass(puppetClass2);
		scParam2.setUsePuppetDefault(true);
		scParam2.setOverrideValues(overrideValues);
		
		final List<SmartClassParameter> scParams = Lists.newArrayList(scParam1, scParam2);
		
		// mock
		Mockito.when(client.getSmartClassParametersWithOverrideValues()).thenReturn(scParams);
		Mockito.when(client.getDetails(scParams)).thenReturn(scParams);
		
		// when
		action.execute();
		
		// then
		Mockito.verify(this.scParamBackupService, Mockito.times(1)).write(paramCaptor.capture());
		final List<SmartClassParameterWrapper> writtenParams = paramCaptor.getAllValues().get(0);		
		assertIsWrapperFor(writtenParams.get(0), scParam1);
		assertIsWrapperFor(writtenParams.get(1), scParam2);

	}
	
	@Test
	public void testExportHostGroupValues() throws IOException {
		
		final String hostGroup1 = "hostGroup1";
		final String hostGroup2 = "hostGroup2";
		
		final SmartClassParameterOverrideValue overrideValue1 = new SmartClassParameterOverrideValue();
		overrideValue1.setMatch(ForemanEntities.buildMatcher(ForemanEntities.TYPE_HOSTGROUP, hostGroup1));
		overrideValue1.setValue("value1");
		overrideValue1.setUsePuppetDefault(false);
		
		final SmartClassParameterOverrideValue overrideValue2 = new SmartClassParameterOverrideValue();
		overrideValue2.setMatch(ForemanEntities.buildMatcher(ForemanEntities.TYPE_HOSTGROUP, hostGroup2));
		overrideValue2.setValue("value2");
		overrideValue2.setUsePuppetDefault(false);
		
		final SmartClassParameter scParam1 = new SmartClassParameter();
		scParam1.setParameter("param1");
		scParam1.setPuppetClass(puppetClass1);
		scParam1.setOverrideValues(Lists.newArrayList(overrideValue1));
		
		final SmartClassParameter scParam2 = new SmartClassParameter();
		scParam2.setParameter("param2");
		scParam2.setPuppetClass(puppetClass2);
		scParam2.setOverrideValues(Lists.newArrayList(overrideValue2));		
		
		final List<SmartClassParameter> scParams = Lists.newArrayList(scParam1, scParam2);
		
		// mock
		Mockito.when(client.getSmartClassParametersWithOverrideValues()).thenReturn(scParams);
		Mockito.when(client.getDetails(scParams)).thenReturn(scParams);
		
		// when
		action.execute();
		
		// then
		Mockito.verify(this.scParamBackupService, Mockito.times(1)).writeHostGroupValues(Matchers.eq(hostGroup1), paramCaptor.capture());
		Mockito.verify(this.scParamBackupService, Mockito.times(1)).writeHostGroupValues(Matchers.eq(hostGroup2), paramCaptor.capture());
		assertIsWrapperFor(paramCaptor.getAllValues().get(0).get(0), scParam1, overrideValue1);
		assertIsWrapperFor(paramCaptor.getAllValues().get(1).get(0), scParam2, overrideValue2);	
		
	}
	
	@Test
	public void testExportHostGroupClasses() throws IOException {
		
		// given
		final ForemanHostGroup group1 = new ForemanHostGroup(1, "group1");
		final List<PuppetClass> classesGroup1 = Lists.newArrayList(puppetClass1);
		
		final ForemanHostGroup group2 = new ForemanHostGroup(2, "group2");
		final List<PuppetClass> classesGroup2 = Lists.newArrayList(puppetClass2);
		
		// mock
		Mockito.when(client.getHostGroups()).thenReturn(Lists.newArrayList(group1, group2));
		Mockito.when(client.getPuppetClassesOfHostGroup(group1)).thenReturn(classesGroup1);
		Mockito.when(client.getPuppetClassesOfHostGroup(group2)).thenReturn(classesGroup2);
		
		// when
		action.execute();
		
		// then
		Mockito.verify(this.modulesBackupService).writeHostGroupClasses(group1.getName(), classesGroup1);
		Mockito.verify(this.modulesBackupService).writeHostGroupClasses(group2.getName(), classesGroup2);
		
	}
	
	@Test
	public void testExportOsValues() throws IOException {
		
		final String os1 = "Debian";
		final String os2 = "Ubuntu";
		
		final SmartClassParameterOverrideValue overrideValue1 = new SmartClassParameterOverrideValue();
		overrideValue1.setMatch(ForemanEntities.buildMatcher(ForemanEntities.TYPE_OS, os1));
		overrideValue1.setValue("value1");
		overrideValue1.setUsePuppetDefault(false);
		
		final SmartClassParameterOverrideValue overrideValue2 = new SmartClassParameterOverrideValue();
		overrideValue2.setMatch(ForemanEntities.buildMatcher(ForemanEntities.TYPE_OS, os2));
		overrideValue2.setValue("value2");
		overrideValue2.setUsePuppetDefault(false);
		
		final SmartClassParameter scParam1 = new SmartClassParameter();
		scParam1.setParameter("param1");
		scParam1.setPuppetClass(puppetClass1);
		scParam1.setOverrideValues(Lists.newArrayList(overrideValue1));
		
		final SmartClassParameter scParam2 = new SmartClassParameter();
		scParam2.setParameter("param2");
		scParam2.setPuppetClass(puppetClass2);
		scParam2.setOverrideValues(Lists.newArrayList(overrideValue2));		
		
		final List<SmartClassParameter> scParams = Lists.newArrayList(scParam1, scParam2);
		
		// mock
		Mockito.when(client.getSmartClassParametersWithOverrideValues()).thenReturn(scParams);
		Mockito.when(client.getDetails(scParams)).thenReturn(scParams);
		
		// when
		action.execute();
		
		// then
		Mockito.verify(this.scParamBackupService, Mockito.times(1)).writeOsValues(Matchers.eq(os1), paramCaptor.capture());
		Mockito.verify(this.scParamBackupService, Mockito.times(1)).writeOsValues(Matchers.eq(os2), paramCaptor.capture());
		assertIsWrapperFor(paramCaptor.getAllValues().get(0).get(0), scParam1, overrideValue1);
		assertIsWrapperFor(paramCaptor.getAllValues().get(1).get(0), scParam2, overrideValue2);	
		
	}
	
	@Test
	public void testExportDomainValues() throws IOException {
		
		final String domain1 = "ksf.local";
		final String domain2 = "metrixware.local";
		
		final SmartClassParameterOverrideValue overrideValue1 = new SmartClassParameterOverrideValue();
		overrideValue1.setMatch(ForemanEntities.buildMatcher(ForemanEntities.TYPE_DOMAIN, domain1));
		overrideValue1.setValue("value1");
		overrideValue1.setUsePuppetDefault(false);
		
		final SmartClassParameterOverrideValue overrideValue2 = new SmartClassParameterOverrideValue();
		overrideValue2.setMatch(ForemanEntities.buildMatcher(ForemanEntities.TYPE_DOMAIN, domain2));
		overrideValue2.setValue("value2");
		overrideValue2.setUsePuppetDefault(false);
		
		final SmartClassParameter scParam1 = new SmartClassParameter();
		scParam1.setParameter("param1");
		scParam1.setPuppetClass(puppetClass1);
		scParam1.setOverrideValues(Lists.newArrayList(overrideValue1));
		
		final SmartClassParameter scParam2 = new SmartClassParameter();
		scParam2.setParameter("param2");
		scParam2.setPuppetClass(puppetClass2);
		scParam2.setOverrideValues(Lists.newArrayList(overrideValue2));		
		
		final List<SmartClassParameter> scParams = Lists.newArrayList(scParam1, scParam2);
		
		// mock
		Mockito.when(client.getSmartClassParametersWithOverrideValues()).thenReturn(scParams);
		Mockito.when(client.getDetails(scParams)).thenReturn(scParams);
		
		// when
		action.execute();
		
		// then
		Mockito.verify(this.scParamBackupService, Mockito.times(1)).writeDomainValues(Matchers.eq(domain1), paramCaptor.capture());
		Mockito.verify(this.scParamBackupService, Mockito.times(1)).writeDomainValues(Matchers.eq(domain2), paramCaptor.capture());
		assertIsWrapperFor(paramCaptor.getAllValues().get(0).get(0), scParam1, overrideValue1);
		assertIsWrapperFor(paramCaptor.getAllValues().get(1).get(0), scParam2, overrideValue2);	
		
	}
	
	@Test
	public void testExportHostClasses() throws IOException {
		
		// given
		final ForemanHost host1 = new ForemanHost("1", "foreman.ksf.local");
		final List<PuppetClass> classesHost1 = Lists.newArrayList(puppetClass1);
		
		final ForemanHost host2 = new ForemanHost("2", "foundation.ksf.local");
		final List<PuppetClass> classesHost2 = Lists.newArrayList(puppetClass2);
		
		// mock
		Mockito.when(client.getHosts()).thenReturn(Lists.newArrayList(host1, host2));
		Mockito.when(client.getHost(host1.getName())).thenReturn(host1);
		Mockito.when(client.getPuppetClassesOfHost(host1)).thenReturn(classesHost1);
		Mockito.when(client.getHost(host2.getName())).thenReturn(host2);
		Mockito.when(client.getPuppetClassesOfHost(host2)).thenReturn(classesHost2);
		
		// when
		action.execute();
		
		// then
		Mockito.verify(this.modulesBackupService, Mockito.times(1)).writeHostClasses(host1, classesHost1);
		Mockito.verify(this.modulesBackupService, Mockito.times(1)).writeHostClasses(host2, classesHost2);
		
	}
	
	@Test
	public void testExportHostValues() throws IOException {
		
		// given
		final ForemanHost host1 = new ForemanHost("1", "foreman.ksf.local");
		final ForemanHost host2 = new ForemanHost("2", "foundation.ksf.local");
		
		final SmartClassParameterOverrideValue overrideValue1 = new SmartClassParameterOverrideValue();
		overrideValue1.setMatch(ForemanEntities.buildMatcher(ForemanEntities.TYPE_FQDN, host1.getName()));
		overrideValue1.setValue("value1");
		overrideValue1.setUsePuppetDefault(false);
		
		final SmartClassParameterOverrideValue overrideValue2 = new SmartClassParameterOverrideValue();
		overrideValue2.setMatch(ForemanEntities.buildMatcher(ForemanEntities.TYPE_FQDN, host2.getName()));
		overrideValue2.setValue("value2");
		overrideValue2.setUsePuppetDefault(false);
		
		final SmartClassParameter scParam1 = new SmartClassParameter();
		scParam1.setParameter("param1");
		scParam1.setPuppetClass(puppetClass1);
		scParam1.setOverrideValues(Lists.newArrayList(overrideValue1));
		
		final SmartClassParameter scParam2 = new SmartClassParameter();
		scParam2.setParameter("param2");
		scParam2.setPuppetClass(puppetClass2);
		scParam2.setOverrideValues(Lists.newArrayList(overrideValue2));		
		
		final List<SmartClassParameter> scParams = Lists.newArrayList(scParam1, scParam2);
		
		// mock
		Mockito.when(client.getHosts()).thenReturn(Lists.newArrayList(host1, host2));
		Mockito.when(client.getHost(host1.getName())).thenReturn(host1);
		Mockito.when(client.getHost(host2.getName())).thenReturn(host2);
		Mockito.when(client.getSmartClassParametersWithOverrideValues()).thenReturn(scParams);
		Mockito.when(client.getDetails(scParams)).thenReturn(scParams);
		
		// when
		action.execute();
		
		// then
		Mockito.verify(this.scParamBackupService, Mockito.times(1)).write(Matchers.eq(host1), paramCaptor.capture());
		Mockito.verify(this.scParamBackupService, Mockito.times(1)).write(Matchers.eq(host2), paramCaptor.capture());
		assertIsWrapperFor(paramCaptor.getAllValues().get(0).get(0), scParam1, overrideValue1);
		assertIsWrapperFor(paramCaptor.getAllValues().get(1).get(0), scParam2, overrideValue2);
		
	}
	
	private void assertIsWrapperFor(final SmartClassParameterWrapper wrapper, final SmartClassParameter param) {
		
		Assert.assertEquals(param.getParameter(), wrapper.getParameter());
		Assert.assertEquals(param.getPuppetClass().getName(), wrapper.getPuppetClass());
		Assert.assertEquals(param.getPuppetClass().getModuleName(), wrapper.getPuppetModule());
		Assert.assertEquals(param.getDefaultValue(), wrapper.getValue());
		Assert.assertEquals(param.isUsePuppetDefault(), wrapper.getUsePuppetDefault());
		
	}
	
	private void assertIsWrapperFor(final SmartClassParameterWrapper wrapper, final SmartClassParameter param, final SmartClassParameterOverrideValue overrideValue) {
		
		Assert.assertEquals(param.getParameter(), wrapper.getParameter());
		Assert.assertEquals(param.getPuppetClass().getName(), wrapper.getPuppetClass());
		Assert.assertEquals(param.getPuppetClass().getModuleName(), wrapper.getPuppetModule());
		Assert.assertEquals(overrideValue.getValue(), wrapper.getValue());
		Assert.assertEquals(overrideValue.getUsePuppetDefault(), wrapper.getUsePuppetDefault());
		
	}
	
}
