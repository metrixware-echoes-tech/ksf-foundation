package fr.echoes.labs.ksf.extensions.pluginloader;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;

import fr.echoes.labs.pluginfwk.api.extension.ExtensionManager;
import fr.echoes.labs.pluginfwk.api.extension.ExtensionSearchResultCache;
import fr.echoes.labs.pluginfwk.api.extension.IExtension;

@Service
public class ExtensionManagerImpl implements ExtensionManager {

	private static final Logger					LOGGER	= LoggerFactory.getLogger(ExtensionManagerImpl.class);

	private final ExtensionEndPoints			extensionEndPoints;

	private final ExtensionSearchResultCache	extensionSearchResultCache;

	@Autowired
	public ExtensionManagerImpl(final ExtensionEndPoints extensionEndPoints, final ExtensionSearchResultCache extensionSearchResultCache) {
		super();
		this.extensionEndPoints = extensionEndPoints;
		this.extensionSearchResultCache = extensionSearchResultCache;
	}

	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.ksf.extensions.pluginloader.ExtensionManager#findExtensions(java.lang.Class)
	 */
	@Override
	public <T extends IExtension> List<T> findExtensions(final Class<T> _endPoint) {
		Validate.notNull(_endPoint);
		final Optional<List<T>> cacheResult = this.extensionSearchResultCache.queryCache(_endPoint);
		if (cacheResult.isPresent()) {
			return cacheResult.get();
		} else {
			final List<T> implementingExtensions = this.extensionEndPoints.findAllImplementingExtensions(_endPoint);
			this.extensionSearchResultCache.storeQuery(_endPoint, implementingExtensions);
			return implementingExtensions;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.ksf.extensions.pluginloader.ExtensionManager#registerExtensions(java.lang.String, fr.echoes.labs.ksf.extensions.api.IExtension[])
	 */
	@Override
	public void registerExtensions(final String pluginID, final IExtension[] extensions) {
		Validate.notEmpty(pluginID, "Plugin ID should not be Empty");
		LOGGER.info("Registering {} extensions from the plugin {}", extensions.length, pluginID);
		for (final IExtension extension : extensions) {
			LOGGER.info("Register the extension {} of the plugin {}", extension, pluginID);
			if (!this.extensionEndPoints.registerExtension(pluginID, extension)) {
				LOGGER.error("Could not register the extension {} of the plugin {}", extension, pluginID);
			} else {
				LOGGER.debug("Extension {} from plugin {} has been registered", extension, pluginID);
			}
		}
	}

}
