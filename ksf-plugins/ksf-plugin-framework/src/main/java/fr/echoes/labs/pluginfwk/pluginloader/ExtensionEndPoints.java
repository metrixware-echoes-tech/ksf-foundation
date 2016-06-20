package fr.echoes.labs.pluginfwk.pluginloader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.echoes.labs.pluginfwk.api.extension.IExtension;

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

	/**
	 * Removes the extensions of plugin.
	 *
	 * @param pluginID
	 *            the plugin id
	 */
	public void removeExtensionsOfPlugin(final String pluginID) {
		Validate.notEmpty(pluginID);
		this.removeIf(new Predicate<RegisteredExtension>() {

			@Override
			public boolean test(final RegisteredExtension t) {
				return pluginID.equals(t.getPluginID());
			}
		});

	}

}
