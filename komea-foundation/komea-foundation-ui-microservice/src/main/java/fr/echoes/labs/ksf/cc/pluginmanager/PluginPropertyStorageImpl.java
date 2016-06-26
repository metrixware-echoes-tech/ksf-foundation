package fr.echoes.labs.ksf.cc.pluginmanager;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.echoes.labs.pluginfwk.api.propertystorage.PluginProperties;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertiesBean;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;

@Service
public class PluginPropertyStorageImpl implements PluginPropertyStorage {

	private static final Logger						LOGGER	= LoggerFactory.getLogger(PluginPropertyStorageImpl.class);

	private final PluginFrameworkConfigurationBean	pluginFrameworkConfigurationBean;

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
	public void initDefaultProperties(final PluginProperties _pluginProperties) {
		this.readPluginProperties(_pluginProperties.getPluginID());
		LOGGER.info("Serialization of {}", _pluginProperties.getPluginID());
		final Map<String, ?> defaultPropertiesMap = _pluginProperties.getPluginProperties();

		final PluginPropertiesBean pluginProperties = this.readPluginProperties(_pluginProperties.getPluginID());

		final Map<String, Object> propertiesAsMap = pluginProperties.getPluginPropertiesAsMap();
		for (final Entry<String, ?> defaultKey : defaultPropertiesMap.entrySet()) {
			if (!propertiesAsMap.containsKey(defaultKey.getKey())) {
				LOGGER.debug("Updating the property {} with {}", defaultKey.getKey(), defaultKey.getValue());
				propertiesAsMap.put(defaultKey.getKey(), defaultKey.getValue());
			}
		}

	}

	public void initFolder() {
		this.pluginFrameworkConfigurationBean.createPluginConfigurationStorageFolder();
	}

	@Override
	public PluginPropertiesBean readPluginProperties(final String _pluginID) {
		final PluginPropertiesBeanImpl propertiesBeanImpl = new PluginPropertiesBeanImpl(this.getPluginFileStorage(_pluginID));
		propertiesBeanImpl.readProperties();
		return propertiesBeanImpl;
	}

	@Override
	public void updatePluginProperties(final PluginProperties _pluginProperties) {
		final PluginPropertiesBeanImpl propertiesBeanImpl = new PluginPropertiesBeanImpl(this.getPluginFileStorage(_pluginProperties.getPluginID()));
		propertiesBeanImpl.setProperties(_pluginProperties);
		propertiesBeanImpl.writeProperties();

	}

}
