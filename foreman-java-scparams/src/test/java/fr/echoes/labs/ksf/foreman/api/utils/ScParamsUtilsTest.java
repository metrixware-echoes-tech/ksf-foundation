package fr.echoes.labs.ksf.foreman.api.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.echoes.labs.ksf.foreman.api.model.PuppetClass;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameter;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterOverrideValue;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterWrapper;

public class ScParamsUtilsTest {

	@Test
	public void testExtractHostGroups() {
		
		final SmartClassParameterOverrideValue value1 = new SmartClassParameterOverrideValue();
		value1.setMatch("fqdn=host1");
		value1.setValue("value1");
		
		final SmartClassParameterOverrideValue value2 = new SmartClassParameterOverrideValue();
		value2.setMatch("hostgroup=group1");
		value2.setValue("value2");
		
		final SmartClassParameterOverrideValue value3 = new SmartClassParameterOverrideValue();
		value3.setMatch("hostgroup=group2");
		value3.setValue("value3");
		
		final SmartClassParameter param1 = new SmartClassParameter();
		param1.setOverrideValues(Lists.newArrayList(value1, value2));
		
		final SmartClassParameter param2 = new SmartClassParameter();
		param2.setOverrideValues(Lists.newArrayList(value3));
		
		final List<SmartClassParameter> params = Lists.newArrayList(param1, param2);
		
		final Set<String> groups = ScParamsUtils.extractHostGroups(params);
		
		Assert.assertEquals(2, groups.size());
		Assert.assertEquals(Sets.newHashSet("group1", "group2"), groups);
	}
	
	@Test
	public void testGetOverrideValueForHostGroup() {
		
		final SmartClassParameterOverrideValue value1 = new SmartClassParameterOverrideValue();
		value1.setMatch("fqdn=host1");
		value1.setValue("value1");
		
		final SmartClassParameterOverrideValue value2 = new SmartClassParameterOverrideValue();
		value2.setMatch("hostgroup=group1");
		value2.setValue("value2");
		
		final SmartClassParameterOverrideValue value3 = new SmartClassParameterOverrideValue();
		value3.setMatch("hostgroup=group2");
		value3.setValue("value3");
		
		final SmartClassParameter param = new SmartClassParameter();
		param.setOverrideValues(Lists.newArrayList(value1, value2, value3));
		
		final SmartClassParameterOverrideValue result = ScParamsUtils.getOverrideValueForHostGroup(param, "group1");
		
		Assert.assertEquals(value2.getValue(), result.getValue());
	}
	
	@Test
	public void testGetOverrideValueForHost() {
		
		final SmartClassParameterOverrideValue value1 = new SmartClassParameterOverrideValue();
		value1.setMatch("fqdn=host1");
		value1.setValue("value1");
		
		final SmartClassParameterOverrideValue value2 = new SmartClassParameterOverrideValue();
		value2.setMatch("fqdn=host2");
		value2.setValue("value2");
		
		final SmartClassParameterOverrideValue value3 = new SmartClassParameterOverrideValue();
		value3.setMatch("hostgroup=group2");
		value3.setValue("value3");
		
		final SmartClassParameter param = new SmartClassParameter();
		param.setOverrideValues(Lists.newArrayList(value1, value2, value3));
		
		final SmartClassParameterOverrideValue result = ScParamsUtils.getOverrideValueForHost(param, "host2");
		
		Assert.assertEquals(value2.getValue(), result.getValue());
	}
	
	@Test
	public void testGroupByHostGroup() {
		
		final SmartClassParameterOverrideValue value1 = new SmartClassParameterOverrideValue();
		value1.setMatch("fqdn=host1");
		value1.setValue("value1");
		
		final SmartClassParameterOverrideValue value2 = new SmartClassParameterOverrideValue();
		value2.setMatch("hostgroup=group1");
		value2.setValue("value2");
		
		final SmartClassParameterOverrideValue value3 = new SmartClassParameterOverrideValue();
		value3.setMatch("hostgroup=group2");
		value3.setValue("value3");
		
		final SmartClassParameter param1 = new SmartClassParameter();
		param1.setPuppetClass(new PuppetClass(1, "puppetclass1", "module1"));
		param1.setOverrideValues(Lists.newArrayList(value1, value2, value3));
		
		final SmartClassParameterOverrideValue value4 = new SmartClassParameterOverrideValue();
		value4.setMatch("fqdn=host1");
		value4.setValue("value4");
		
		final SmartClassParameterOverrideValue value5 = new SmartClassParameterOverrideValue();
		value5.setMatch("hostgroup=group1");
		value5.setValue("value5");
		
		final SmartClassParameterOverrideValue value6 = new SmartClassParameterOverrideValue();
		value6.setMatch("hostgroup=group2");
		value6.setValue("value6");
		
		final SmartClassParameter param2 = new SmartClassParameter();
		param2.setPuppetClass(new PuppetClass(2, "puppetclass2", "module2"));
		param2.setOverrideValues(Lists.newArrayList(value4, value5, value6));
		
		final Map<String, List<SmartClassParameterWrapper>> results = ScParamsUtils.groupByHostGroup(Lists.newArrayList(param1, param2));
		
		Assert.assertEquals(2, results.size());
		
	}
	
	@Test
	public void testNewOverrideValue() {
		
		final SmartClassParameterWrapper value = new SmartClassParameterWrapper();
		value.setValue("myValue");
		value.setUsePuppetDefault(false);
		
		final String entityType = ForemanEntities.TYPE_FQDN;
		final String entityName = "myHost";
		
		final SmartClassParameterOverrideValue result = ScParamsUtils.newOverrideValue(value, entityType, entityName);
		
		Assert.assertEquals(entityType+"="+entityName, result.getMatch());
		Assert.assertEquals(value.getValue(), result.getValue());
		Assert.assertEquals(value.getUsePuppetDefault(), result.getUsePuppetDefault());
		
	}
	
	@Test
	public void testFindByPuppetClassId() {
		
		final Integer puppetClassId = 35;
		final String parameter = "myParam";
		final List<SmartClassParameter> params = Lists.newArrayList();

		SmartClassParameter result = ScParamsUtils.findByPuppetClassId(params, puppetClassId, parameter);	
		Assert.assertNull(result);
		
		final SmartClassParameter param1 = new SmartClassParameter();
		param1.setParameter("aUselessParam");
		param1.setPuppetClass(new PuppetClass(1, "pc1", "m1"));
		params.add(param1);
		
		result = ScParamsUtils.findByPuppetClassId(params, puppetClassId, parameter);
		Assert.assertNull(result);
		
		final SmartClassParameter param2 = new SmartClassParameter();
		param2.setParameter(parameter);
		param2.setPuppetClass(new PuppetClass(34, "pc1", "m1"));
		params.add(param2);
		
		result = ScParamsUtils.findByPuppetClassId(params, puppetClassId, parameter);
		Assert.assertNull(result);
		
		final SmartClassParameter param3 = new SmartClassParameter();
		param3.setParameter(parameter);
		param3.setPuppetClass(new PuppetClass(35, "pc1", "m1"));
		params.add(param3);
		
		result = ScParamsUtils.findByPuppetClassId(params, puppetClassId, parameter);
		Assert.assertEquals(params.get(2), result);
		
	}
	
}
