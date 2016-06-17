package fr.echoes.labs.ksf.extensions.pluginloader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import fr.echoes.labs.pluginfwk.api.extension.ExtensionSearchResultCache;

@Component
public class ExtensionSearchResultsCacheImpl implements ExtensionSearchResultCache {

	private static final Logger			LOGGER	= LoggerFactory.getLogger(ExtensionSearchResultsCacheImpl.class);

	private final Cache<String, List>	cache;

	/**
	 * Instantiates a new extension search results cache.
	 */
	public ExtensionSearchResultsCacheImpl() {
		this.cache = CacheBuilder.newBuilder().build();
	}

	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.ksf.extensions.pluginloader.ExtensionSearchResultCache#cleanUp()
	 */
	@Override
	public void cleanUp() {
		this.cache.cleanUp();
	}

	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.ksf.extensions.pluginloader.ExtensionSearchResultCache#queryCache(java.lang.Class)
	 */
	@Override
	public <T> Optional<List<T>> queryCache(final Class<T> _endPoint) {
		if (_endPoint == null) {
			return Optional.absent();
		}
		LOGGER.debug("Retrieving extensions with key {}", _endPoint);
		final List<T> present = this.cache.getIfPresent(_endPoint.getName());
		return Optional.fromNullable(present);
	}

	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.ksf.extensions.pluginloader.ExtensionSearchResultCache#storeQuery(java.lang.Class, java.util.List)
	 */
	@Override
	public <T> void storeQuery(final Class<T> _endPoint, final List<T> implementingExtensions) {
		LOGGER.debug("Storing the extensions with the key {}", _endPoint);
		this.cache.put(_endPoint.getName(), Collections.unmodifiableList(new ArrayList<>(implementingExtensions)));

	}
}
