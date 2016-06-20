package fr.echoes.labs.pluginfwk.api.plugin;

public class PluginException extends RuntimeException {

	public PluginException() {
		//
		super("The plugin framework encountered a problem.");
	}

	public PluginException(final String _message) {
		super(_message);
		//
	}

	public PluginException(final String _message, final Throwable _cause) {
		super(_message, _cause);
		//
	}

}
