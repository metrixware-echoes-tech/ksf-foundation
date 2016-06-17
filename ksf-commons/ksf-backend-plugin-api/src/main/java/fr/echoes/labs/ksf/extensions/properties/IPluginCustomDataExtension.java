package fr.echoes.labs.ksf.extensions.properties;

import fr.echoes.labs.ksf.extensions.api.IExtension;

/**
 * This interface allows a plugin to store custom data into the backend.
 *
 * @author sleroy
 *
 */
public interface IPluginCustomDataExtension extends IExtension{
	/**
	 * Allows a plugin to declare a custom data dao into the backend.
	 *
	 * @param _factory
	 *            the factory.
	 */
	void declareCustomDataDAO(ICustomDataDAOFactory _factory);
}
