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
		if (_pluginProperties == null) {
			return;
		}
		if (this.getPluginFileStorage(_pluginProperties.getPluginID()).exists()) {
			return;
		}
		this.configurationCache.invalidate(_pluginProperties.getPluginID());
		final PluginPropertiesBeanImpl propertiesBeanImpl = new PluginPropertiesBeanImpl(this.getPluginFileStorage(_pluginProperties.getPluginID()));
		propertiesBeanImpl.writeProperties(_pluginProperties.getPluginProperties());

	}

	public void initFolder() {
		this.pluginFrameworkConfigurationBean.createPluginConfigurationStorageFolder();
	}

	@Override
	public synchronized <T> T readPluginProperties(final String _pluginID) {
		T cacheObject = (T) this.configurationCache.getIfPresent(_pluginID);
		if (cacheObject == null) {
			final File pluginFileStorage = this.getPluginFileStorage(_pluginID);
			final PluginPropertiesBeanImpl propertiesBeanImpl = new PluginPropertiesBeanImpl(pluginFileStorage);
			cacheObject = propertiesBeanImpl.readProperties();
			this.configurationCache.put(_pluginID, cacheObject);
		}
		return cacheObject;
	}

	@Override
	public synchronized void updatePluginProperties(final PluginProperties _pluginProperties) {
		this.configurationCache.put(_pluginProperties.getPluginID(), _pluginProperties);
		final PluginPropertiesBeanImpl propertiesBeanImpl = new PluginPropertiesBeanImpl(this.getPluginFileStorage(_pluginProperties.getPluginID()));
		propertiesBeanImpl.writeProperties(_pluginProperties.getPluginProperties());

	}

}
