package fr.echoes.labs.ksf.foreman.api.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PageAggregatorTest {

	@Test
	public void testAggregate() throws IOException {
		
		// given
		final Map<Integer, List<String>> pages = Maps.newHashMap();
		pages.put(1, Lists.newArrayList("o1", "o2"));
		pages.put(2, Lists.newArrayList("o3"));
		pages.put(3, Lists.newArrayList("o4"));
		pages.put(4, new ArrayList<String>());
		
		// and
		final PageAggregator<String> pageAggregator = new PageAggregator<String>() {
			@Override
			public List<String> executeRequest(int page) throws IOException {
				return pages.get(page);
			}
		};
		
		// when
		final List<String> results = pageAggregator.execute();
		
		// then
		Assert.assertEquals(Lists.newArrayList("o1", "o2", "o3", "o4"), results);
		
	}
	
}
