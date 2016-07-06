package fr.echoes.labs.ksf.cc.pluginmanager;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import fr.echoes.labs.pluginfwk.api.propertystorage.PluginProperties;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;

@Service
public class PluginPropertyStorageImpl implements PluginPropertyStorage {

	private static final Logger						LOGGER	= LoggerFactory.getLogger(PluginPropertyStorageImpl.class);

	private final PluginFrameworkConfigurationBean	pluginFrameworkConfigurationBean;

	private final Cache<String, Object>				configurationCache;

	/**
	 * Instantiates a new ksf plugin property storage.
	 *
	 * @param _pluginFrameworkConfigurationBean
	 *            the plugin framework configuration bean
	 */
	@Autowired
	public PluginPropertyStorageImpl(final PluginFrameworkConfigurationBean _pluginFrameworkConfigurationBean) {
		super();
		this.pluginFrameworkConfigurationBean = _pluginFrameworkConfigurationBean;
		// Automatically retrieves the configuration.
		this.configurationCache = CacheBuilder.newBuilder().build();

	}

	/**
	 * Gets the plugin file storage.
	 *
	 * @param pluginID
	 *            the plugin ID
	 * @return the plugin file storage
	 */
	public File getPluginFileStorage(final String pluginID) {
		return new File(this.getPluginFileStorageFolder(pluginID), "configuration.json");
	}

	/**
	 * Gets the plugin file storage.
	 *
	 * @param pluginID
	 *            the plugin ID
	 * @return the plugin file storage
	 */
	@Override
	public File getPluginFileStorageFolder(final String pluginID) {
		final File file = new File(this.pluginFrameworkConfigurationBean.getPluginPropertyStorageLocation(), pluginID);
		if (!file.exists() && !file.mkdirs()) {
			LOGGER.warn("Could not create the folder to store the properties of the plugin {} -> {}", pluginID, file);
		}
		return file;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage#initDefaultProperties(fr.echoes.labs.pluginfwk.api.propertystorage.PluginProperties)
	 */
	@Override
	public synchronized void initDefaultProperties(final PluginProperties _pluginProperties) {
		LOGGER.debug("Initialization of the default properties for the plugin {}", _pluginProperties);
		if (_pluginProperties == null || _pluginProperties.getPluginProperties() == null) {
			LOGGER.debug("No need to define default properties for {}", _pluginProperties);
			return;
		}
		final Object pluginProperties = this.readPluginProperties(_pluginProperties.getPluginID(), Object.class);
		if (pluginProperties != null) {

			// Configuration already existing.
			return;
		}
		this.configurationCache.invalidate(_pluginProperties.getPluginID());
		final PluginPropertiesBeanImpl propertiesBeanImpl = new PluginPropertiesBeanImpl(this.getPluginFileStorage(_pluginProperties.getPluginID()));
		propertiesBeanImpl.writeProperties(_pluginProperties.getPluginProperties());

	}

	/**
	 * Inits the folder.
	 */
	public void initFolder() {
		LOGGER.debug("Initialization of the plugin configuration folder");
		this.pluginFrameworkConfigurationBean.createPluginConfigurationStorageFolder();
	}

	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage#readPluginProperties(java.lang.String)
	 */
	@Override
	public synchronized <T> T readPluginProperties(final String _pluginID, final Class<T> _expectedObject) {
		LOGGER.debug("Trying to read plugin properties {}", _pluginID);
		T cacheObject = (T) this.configurationCache.getIfPresent(_pluginID);
		if (cacheObject == null) {
			final File pluginFileStorage = this.getPluginFileStorage(_pluginID);
			if (!pluginFileStorage.exists()) {
				LOGGER.warn("No configuration file found for the plugin {}", _pluginID);
				return null;
			} else {

				LOGGER.debug("Reading properties for {}", _pluginID);
				final PluginPropertiesBeanImpl propertiesBeanImpl = new PluginPropertiesBeanImpl(pluginFileStorage);
				cacheObject = propertiesBeanImpl.readProperties(_expectedObject);
				if (cacheObject != null) {
					this.configurationCache.put(_pluginID, cacheObject);
				} else {
					LOGGER.debug("Nothing to store into the cache of {}", _pluginID);
				}
			}
		}
		return cacheObject;
	}

	@Override
	public synchronized void updatePluginProperties(final PluginProperties _pluginProperties) {
		LOGGER.info("Updating properties of the plugin {}", _pluginProperties.getPluginID());
		final Object pluginProperties = _pluginProperties.getPluginProperties();
		if (pluginProperties != null) {
			this.configurationCache.put(_pluginProperties.getPluginID(), pluginProperties);
			this.writeProperties(_pluginProperties, pluginProperties);
		} else {
			this.configurationCache.invalidate(_pluginProperties.getPluginID());
			this.deleteProperties(_pluginProperties);
		}

	}

	private void deleteProperties(final PluginProperties _pluginProperties) {
		LOGGER.warn("Deleting properties of {}", _pluginProperties.getPluginID());
		this.getPluginFileStorage(_pluginProperties.getPluginID()).delete();

	}

	private void writeProperties(final PluginProperties _pluginProperties, final Object pluginProperties) {
		LOGGER.info("Writing properties of {}", _pluginProperties.getPluginID());
		final PluginPropertiesBeanImpl propertiesBeanImpl = new PluginPropertiesBeanImpl(this.getPluginFileStorage(_pluginProperties.getPluginID()));
		propertiesBeanImpl.writeProperties(pluginProperties);
	}

}
