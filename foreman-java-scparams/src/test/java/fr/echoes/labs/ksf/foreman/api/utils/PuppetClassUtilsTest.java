package fr.echoes.labs.ksf.foreman.api.utils;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.foreman.api.model.ForemanHostGroup;
import fr.echoes.labs.ksf.foreman.api.model.PuppetClass;

public class PuppetClassUtilsTest {

	@Test
	public void testFindPuppetClass() {
		
		final List<PuppetClass> puppetClasses = Lists.newArrayList(
				new PuppetClass(1, "class1", "module1"),
				new PuppetClass(2, "class2", "module2"),
				new PuppetClass(3, "class3", "module3")
		);
		
		final PuppetClass result = PuppetClassUtils.findPuppetClass(puppetClasses, "class2", "module2");
		
		Assert.assertEquals(puppetClasses.get(1), result);
	}
	
	@Test
	public void testFindPuppetClassById() {
		
		final List<PuppetClass> puppetClasses = Lists.newArrayList(
				new PuppetClass(1, "class1", "module1"),
				new PuppetClass(2, "class2", "module2"),
				new PuppetClass(3, "class3", "module3")
		);
		
		final PuppetClass result = PuppetClassUtils.findPuppetClassById(puppetClasses, 2);
		
		Assert.assertEquals(puppetClasses.get(1), result);
		
	}
	
	@Test
	public void testHasHostGroup() {
		
		final ForemanHostGroup hostGroup = new ForemanHostGroup();
		hostGroup.setId(35);
		hostGroup.setName("group1");
		
		final PuppetClass puppetClass = new PuppetClass();
		
		Assert.assertFalse(PuppetClassUtils.hasHostGroup(puppetClass, hostGroup));
		
		puppetClass.setHostGroups(Lists.newArrayList(hostGroup));
		
		Assert.assertTrue(PuppetClassUtils.hasHostGroup(puppetClass, hostGroup));
		
		puppetClass.getHostGroups().add(new ForemanHostGroup());
		
		Assert.assertTrue(PuppetClassUtils.hasHostGroup(puppetClass, hostGroup));
		
	}
	
}
