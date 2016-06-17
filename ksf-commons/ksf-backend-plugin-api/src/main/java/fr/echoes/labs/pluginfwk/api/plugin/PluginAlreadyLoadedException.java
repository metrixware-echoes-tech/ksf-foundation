package fr.echoes.labs.pluginfwk.api.plugin;

public class PluginAlreadyLoadedException extends PluginException {

	public PluginAlreadyLoadedException(final PluginInformations pluginDefinition) {
		super("Plugin " + pluginDefinition.getName() + " with id " + pluginDefinition.getId() + "is already loaded.");
	}

}
