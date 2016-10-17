package fr.echoes.labs.ksf.foreman.api.utils;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * Aggregates all the results of paginated API requests.
 * @author dmichel
 * @param <T> : the results type
 */
public abstract class PageAggregator<T> {

	public abstract List<T> executeRequest(int page) throws IOException;
	
	public List<T> execute() throws IOException {
		
		int page = 1;
		
		List<T> pageResults = executeRequest(page);
		
		final List<T> results =  Lists.newArrayList(pageResults);
		
		while (!pageResults.isEmpty()) {
			page++;
			pageResults = executeRequest(page);
			results.addAll(pageResults);
		}
		
		return results;
	}
	
}
