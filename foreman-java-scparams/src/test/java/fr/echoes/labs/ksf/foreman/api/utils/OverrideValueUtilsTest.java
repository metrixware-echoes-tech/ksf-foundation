package fr.echoes.labs.ksf.foreman.api.utils;

import org.junit.Assert;
import org.junit.Test;

public class OverrideValueUtilsTest {

	@Test
	public void testFormatOverrideValueHash() {
		
		// given
		final String value = "test:\\n\\rsuccess:\\\"yes\\\""; 
		final String type = "hash";
		
		// when
		final String result = OverrideValueUtils.formatOverrideValue(value, type);
		
		// then
		Assert.assertNotNull(result);
		Assert.assertFalse(result.contains("\\n"));
		Assert.assertFalse(result.contains("\\r"));
		Assert.assertFalse(result.contains("\\\""));
	}
	
	@Test
	public void testFormatOverrideValue() {
		
		// given
		final String value = "blblbl"; 
		final String type = "hash";
		
		// when
		final String result = OverrideValueUtils.formatOverrideValue(value, type);
		
		// then
		Assert.assertEquals(value, result);
	}
	
}
