package fr.echoes.labs.ksf.foreman.api.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Charsets;

import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameter;

public class OverrideValueUtilsTest { 
	
	@Test
	public void testFormatOverrideValueHash() {
		
		// given
		final String value = "test:\\r\\nsuccess:\\\"yes\\\""; 
		
		// when
		final String result = OverrideValueUtils.formatOverrideValue(value, SmartClassParameter.TYPE_HASH);
		
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
		
		// when
		final String result = OverrideValueUtils.formatOverrideValue(value, SmartClassParameter.TYPE_HASH);
		
		// then
		Assert.assertEquals(value, result);
	}
	
	@Test
	public void testFormatOverrideValueHash2() throws IOException {
		
		// given
		final String value = FileUtils.readFileToString(new File("src/test/resources/test.txt"), Charsets.UTF_8.name());
		
		// when
		final String result = OverrideValueUtils.formatOverrideValue(value, SmartClassParameter.TYPE_HASH);
		
		// then
		Assert.assertNotNull(result);
		//Assert.assertFalse(result.contains("\\\\\\"));
		
	}
	
	@Test
	public void testFormatOverrideValueHash3() throws IOException {
		
		//given
		final String value = FileUtils.readFileToString(new File("src/test/resources/test2.txt"), Charsets.UTF_8.name());
		
		// when
		final String result = OverrideValueUtils.formatOverrideValue(value, SmartClassParameter.TYPE_HASH);
		
		// then
		System.out.println(result);
		Assert.assertFalse(result.contains("\\\n"));
		
	}
	
}
