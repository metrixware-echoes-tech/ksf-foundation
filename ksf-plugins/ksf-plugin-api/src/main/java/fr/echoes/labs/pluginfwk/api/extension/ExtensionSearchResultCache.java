package fr.echoes.labs.pluginfwk.api.extension;

import java.util.List;
import java.util.Optional;

public interface ExtensionSearchResultCache {

	/**
	 * Clean up the cache.
	 */
	void cleanUp();

	/**
	 * Query cache for a result.
	 *
	 * @param <T>
	 *            the generic type
	 * @param _endPoint
	 *            the _end point
	 * @return the optional
	 */
	<T> Optional<List<T>> queryCache(Class<T> _endPoint);

	/**
	 * Store the query into the cache.
	 *
	 * @param <T>
	 *            the generic type
	 * @param _endPoint
	 *            the _end point
	 * @param implementingExtensions
	 *            the implementing extensions
	 */
	<T> void storeQuery(Class<T> _endPoint, List<T> implementingExtensions);

}