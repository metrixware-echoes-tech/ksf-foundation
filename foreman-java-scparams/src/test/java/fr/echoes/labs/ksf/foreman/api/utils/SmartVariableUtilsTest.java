package fr.echoes.labs.ksf.foreman.api.utils;

import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterOverrideValue;
import fr.echoes.labs.ksf.foreman.api.model.SmartVariable;

public class SmartVariableUtilsTest {

	@Test
	public void testExtractHostGroupNames() {
		
		// given
		final String hostGroup1 = "group1";
		final String hostGroup2 = "group2";
		final String hostGroup3 = "group3";
		
		// and
		final SmartVariable var1 = new SmartVariable();
		var1.setId(1);
		var1.setOverrideValues(Lists.newArrayList(
				newOverrideValueForMatch(ForemanEntities.hostGroupMatcher(hostGroup1)),
				newOverrideValueForMatch(ForemanEntities.hostMatcher("randomHost"))
		));
		
		// and
		final SmartVariable var2 = new SmartVariable();
		var2.setId(2);
		var2.setOverrideValues(Lists.newArrayList(
				newOverrideValueForMatch(ForemanEntities.hostGroupMatcher(hostGroup1)),
				newOverrideValueForMatch(ForemanEntities.hostGroupMatcher(hostGroup2)), 
				newOverrideValueForMatch(ForemanEntities.hostGroupMatcher(hostGroup3))
		));
		
		// and
		final SmartVariable var3 = new SmartVariable();
		var3.setId(3);
		var3.setOverrideValues(Lists.<SmartClassParameterOverrideValue>newArrayList());
		
		// and
		final List<SmartVariable> variables = Lists.newArrayList(var1, var2);
		
		// when
		final Set<String> results = SmartVariableUtils.extractHostGroupNames(variables);
		
		// then
		Assert.assertEquals(3, results.size());
		Assert.assertTrue(results.containsAll(Lists.newArrayList(hostGroup1, hostGroup2, hostGroup3)));
	}
	
	@Test
	public void testGetOverrideValueForHostGroup() {
		
		final String hostGroupName = "myHostGroup";
		final SmartClassParameterOverrideValue overrideValue = newOverrideValueForMatch(ForemanEntities.hostGroupMatcher(hostGroupName));
		
		final SmartVariable var1 = new SmartVariable();
		var1.setId(1);
		var1.setOverrideValues(Lists.newArrayList(
				overrideValue,
				newOverrideValueForMatch(ForemanEntities.hostMatcher("randomHost"))
		));
		
		final SmartClassParameterOverrideValue result = SmartVariableUtils.getOverrideValueForHostGroup(var1, hostGroupName);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(overrideValue, result);
	}
	
	private static SmartClassParameterOverrideValue newOverrideValueForMatch(final String match) {
		
		final SmartClassParameterOverrideValue overrideValue = new SmartClassParameterOverrideValue();
		overrideValue.setMatch(match);
		
		return overrideValue;
	}
	
}
