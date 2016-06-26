package fr.echoes.labs.ksf.cc.pluginmanager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.support.DefaultConversionService;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.echoes.labs.pluginfwk.api.propertystorage.PluginProperties;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertiesBean;

public class PluginPropertiesBeanImpl implements PluginPropertiesBean {

	public static class PluginPropertiesDTO {

		private Map<String, Object> properties = new HashMap<>();

		public void clear() {
			this.properties.clear();
		}

		public Map<String, Object> getProperties() {
			return this.properties;
		}

		public Object getValue(final String key) {
			return this.properties.get(key);
		}

		public boolean hasKey(final String name) {
			return this.properties.containsKey(name);

		}

		public void putAll(final Map<String, ?> pluginProperties) {
			this.properties.putAll(pluginProperties);
		}

		public void setProperties(final Map<String, Object> properties) {
			this.properties = properties;
		}

	}

	private static final Logger			LOGGER				= LoggerFactory.getLogger(PluginPropertiesBeanImpl.class);

	private final File					pluginFileStorage;

	private final ObjectMapper			objectMapper;

	private final PluginPropertiesDTO	pluginPropertiesDTO	= new PluginPropertiesDTO();

	public PluginPropertiesBeanImpl(final File pluginFileStorage) {
		this.pluginFileStorage = pluginFileStorage;
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public <T> T convertAsBean(final Class<T> _bean) {
		final T bean = BeanUtils.instantiate(_bean);
		final DefaultConversionService defaultConversionService = new DefaultConversionService();
		final BeanMap beanMap = new BeanMap(bean);

		final Iterator<String> keyIterator = beanMap.keyIterator();
		while (keyIterator.hasNext()) {
			final String key = keyIterator.next();
			if (this.pluginPropertiesDTO.hasKey(key)) {
				final Object value = this.pluginPropertiesDTO.getValue(key);
				final Class<?> fieldType = beanMap.getType(key);
				final Object convertedValue = defaultConversionService.convert(value, fieldType);
				beanMap.put(key, convertedValue);
			}

		}
		return bean;
	}

	@Override
	public Map<String, Object> getPluginPropertiesAsMap() {
		return this.pluginPropertiesDTO.properties;
	}

	public void readProperties() {
		if (!this.pluginFileStorage.exists()) {
			LOGGER.warn("Nothing to read in {}", this.pluginFileStorage);
			return;
		}
		try {
			final PluginPropertiesDTO pluginPropertiesDTO2 = this.objectMapper.readValue(FileUtils.openInputStream(this.pluginFileStorage),
					PluginPropertiesDTO.class);
			this.pluginPropertiesDTO.putAll(pluginPropertiesDTO2.properties);
		} catch (final IOException e) {
			LOGGER.warn("Impossible to read properties from {}", this.pluginFileStorage, e);
		}

	}

	public void setProperties(final Map<String, Object> hashMap) {
		this.pluginPropertiesDTO.clear();
		this.pluginPropertiesDTO.putAll(hashMap);

	}

	public void setProperties(final PluginProperties _pluginProperties) {
		this.pluginPropertiesDTO.clear();
		this.pluginPropertiesDTO.putAll(_pluginProperties.getPluginProperties());
	}

	/**
	 * Write properties into the file.
	 */
	public void writeProperties() {
		try {
			this.objectMapper.writeValue(FileUtils.openOutputStream(this.pluginFileStorage), this.pluginPropertiesDTO);
		} catch (final IOException e) {
			LOGGER.warn("Impossible to write properties {}", this.pluginFileStorage, e);
		}
	}

}
