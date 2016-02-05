package fr.echoes.labs.ksf.plugins.service.api;

import java.util.Properties;

public interface IPluginPropertiesService {

	/**
	 * Returns the properties( if any) for the given plugin referenced by its
	 * id.
	 *
	 * @param _pluginId
	 * @return the properties.
	 */
	Properties getPluginProperties(String _pluginId);
}
