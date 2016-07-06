package fr.echoes.labs.ksf.cc.pluginmanager;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.javers.common.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PluginPropertiesBeanImpl {

	private static final Logger	LOGGER	= LoggerFactory.getLogger(PluginPropertiesBeanImpl.class);

	private final File			pluginFileStorage;

	private final ObjectMapper	objectMapper;

	private Object				data;

	/**
	 * Instantiates a new plugin properties bean impl.
	 *
	 * @param pluginFileStorage
	 *            the plugin file storage
	 */
	public PluginPropertiesBeanImpl(final File pluginFileStorage) {
		this.pluginFileStorage = pluginFileStorage;
		this.objectMapper = new ObjectMapper();
	}

	/**
	 * Gets the data.
	 *
	 * @param <T>
	 *            the generic type
	 * @return the data
	 */
	public <T> T getData() {
		return (T) this.data;
	}

	/**
	 * Read properties.
	 *
	 * @param <T>
	 *            the generic type
	 * @return the t
	 */
	public <T> T readProperties(final Class<T> _expectedType) {
		Validate.argumentIsNotNull(_expectedType);
		if (this.pluginFileStorage.exists()) {

			try {
				this.data = this.objectMapper.readValue(FileUtils.openInputStream(this.pluginFileStorage), _expectedType);
				LOGGER.info("Read data with type {} from plugin {}", _expectedType.getName(), this.pluginFileStorage);
			} catch (final Exception e) {
				LOGGER.warn("Impossible to read properties from {}", this.pluginFileStorage, e);
			}

		} else {
			LOGGER.warn("Nothing to read in {}", this.pluginFileStorage);
		}
		return (T) this.data;

	}

	/**
	 * Write properties into the file.
	 */
	public void writeProperties(final Object _data) {
		try {
			this.data = _data;
			this.objectMapper.writerWithDefaultPrettyPrinter().writeValue(FileUtils.openOutputStream(this.pluginFileStorage), this.data);
		} catch (final Exception e) {
			LOGGER.warn("Impossible to write properties {}", this.pluginFileStorage, e);
			this.pluginFileStorage.delete();
		}
	}

}
