package fr.echoes.labs.ksf.foreman.api.utils;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.foreman.api.model.PuppetClass;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterWrapper;

public class ComparatorsTest {

	@Test
	public void testSmartClassParameterWrapperComparator() {
		
		// given
		final PuppetClass puppetClass1 = new PuppetClass(1, "class1", "module1");
		final PuppetClass puppetClass2 = new PuppetClass(2, "class2", "module1");
		final PuppetClass puppetClass3 = new PuppetClass(3, "class1", "module2");
		
		// and
		final List<SmartClassParameterWrapper> params = Lists.newArrayList(
				new SmartClassParameterWrapper("C", "", puppetClass3, "", false),
				new SmartClassParameterWrapper("B", "", puppetClass2, "", false),
				new SmartClassParameterWrapper("A", "", puppetClass2, "", false),
				new SmartClassParameterWrapper("A", "", puppetClass1, "", false)
		);
		
		// when
		final List<SmartClassParameterWrapper> sorted = Lists.newArrayList(params);
		Collections.sort(sorted, Comparators.smartClassParameterComparator());
		
		// then
		Assert.assertEquals(sorted.get(0), params.get(3));
		Assert.assertEquals(sorted.get(1), params.get(2));
		Assert.assertEquals(sorted.get(2), params.get(1));
		Assert.assertEquals(sorted.get(3), params.get(0));
	}
	
}
