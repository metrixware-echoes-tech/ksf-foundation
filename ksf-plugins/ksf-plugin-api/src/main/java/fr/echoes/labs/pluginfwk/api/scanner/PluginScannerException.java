package fr.echoes.labs.pluginfwk.api.scanner;

import fr.echoes.labs.pluginfwk.api.plugin.PluginException;

public class PluginScannerException extends PluginException {

	private static final String COULD_NOT_LOAD_PLUGINS_FROM = "Could not load plugins from ";

	public PluginScannerException(final String _message) {
		super(COULD_NOT_LOAD_PLUGINS_FROM + _message);
	}

	public PluginScannerException(final String _message, final Throwable _cause) {
		super(COULD_NOT_LOAD_PLUGINS_FROM + _message, _cause);
	}

}
