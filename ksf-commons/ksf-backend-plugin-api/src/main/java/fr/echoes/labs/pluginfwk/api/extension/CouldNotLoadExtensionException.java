package fr.echoes.labs.pluginfwk.api.extension;

import fr.echoes.labs.pluginfwk.api.plugin.PluginException;

public class CouldNotLoadExtensionException extends PluginException {

	public CouldNotLoadExtensionException() {
		// TODO Auto-generated constructor stub
	}

	public CouldNotLoadExtensionException(final String _message) {
		super(_message);
		// TODO Auto-generated constructor stub
	}

	public CouldNotLoadExtensionException(final String extensionType, final String extensionID) {
		super("Could not load the extension of type " + extensionType + " ID : " + extensionID);
	}

	public CouldNotLoadExtensionException(final String _message, final Throwable _cause) {
		super(_message, _cause);
		// TODO Auto-generated constructor stub
	}

}
