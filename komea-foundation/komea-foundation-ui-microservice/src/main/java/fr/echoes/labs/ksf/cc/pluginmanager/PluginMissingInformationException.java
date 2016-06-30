package fr.echoes.labs.ksf.cc.pluginmanager;

import fr.echoes.labs.pluginfwk.api.plugin.PluginException;

public class PluginMissingInformationException extends PluginException {

	public PluginMissingInformationException() {
		super();
	}

	public PluginMissingInformationException(final String _message) {
		super(_message);
	}

	public PluginMissingInformationException(final String _message, final Throwable _cause) {
		super(_message, _cause);
	}

}
