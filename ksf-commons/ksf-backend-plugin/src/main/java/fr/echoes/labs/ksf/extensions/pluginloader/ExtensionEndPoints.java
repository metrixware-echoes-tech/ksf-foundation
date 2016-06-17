package fr.echoes.labs.ksf.extensions.pluginloader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.echoes.labs.pluginfwk.api.extension.IExtension;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExtensionEndPoints extends HashSet<RegisteredExtension> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionEndPoints.class);

	public <T> List<T> findAllImplementingExtensions(final Class<T> _endPoint) {
		final List<T> matchingExtensions = new ArrayList<>();
		for (final RegisteredExtension registeredExtension : this) {
			if (_endPoint.isAssignableFrom(registeredExtension.getExtension().getClass())) {
				LOGGER.debug("Extension {} from {} implements the endPoint {}", registeredExtension.getPluginID(), registeredExtension.getExtension(),
						_endPoint);
				matchingExtensions.add(_endPoint.cast(registeredExtension.getExtension()));
			} else {
				// Not matching endpoint
			}
		}
		return Collections.unmodifiableList(matchingExtensions);
	}

	/**
	 * Register extension.
	 *
	 * @param pluginID
	 *            the plugin id
	 * @param extension
	 *            the extension
	 * @return true, if successful
	 */
	public boolean registerExtension(final String pluginID, final IExtension extension) {
		return this.add(new RegisteredExtensionImpl(pluginID, extension));

	}

}
