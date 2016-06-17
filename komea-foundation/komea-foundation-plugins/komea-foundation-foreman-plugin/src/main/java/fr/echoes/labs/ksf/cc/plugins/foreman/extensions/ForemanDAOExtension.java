package fr.echoes.labs.ksf.cc.plugins.foreman.extensions;

import fr.echoes.labs.pluginfwk.api.plugin.Extension;
import fr.echoes.labs.pluginfwk.api.plugin.propertystorage.ICustomDataDAOFactory;
import fr.echoes.labs.pluginfwk.api.plugin.propertystorage.IPluginCustomDataExtension;

@Extension
public class ForemanDAOExtension implements IPluginCustomDataExtension {

	@Override
	public void declareCustomDataDAO(final ICustomDataDAOFactory _factory) {
	}

}
