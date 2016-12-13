package fr.echoes.labs.ksf.foundation.utils;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class URLUtilsTest {

	@Test
	public void testAddPath() {
		
		// given
		final String url = "https://ksf-dev.metrixware.local";
		final String path = "foreman";
		
		// when
		String result = URLUtils.addPath(url, path);
		
		// then
		Assert.assertEquals(url+'/'+path, result);
		
		// when
		result = URLUtils.addPath(url+'/', path);
		
		// then
		Assert.assertEquals(url+'/'+path, result);
		
		// when
		result = URLUtils.addPath(url, '/'+path);
		
		// then
		Assert.assertEquals(url+'/'+path, result);
		
		// when
		result = URLUtils.addPath(url+'/', '/'+path);
		
		// then
		Assert.assertEquals(url+'/'+path, result);
		
	}
	
	@Test
	public void testBuildQuery() {
		
		final String key1 = "search";
		final String value1 = "my que::ry";
		
		final String key2 = "page";
		final String value2 = "1";
		
		final String query = URLUtils.buildQuery(ImmutableMap.of(key1, value1, key2, value2));
		
		Assert.assertEquals(key1+"="+value1.replace(" ", "+").replace(":", "%3A")+"&"+key2+"="+value2, query);
		
	}
	
}
