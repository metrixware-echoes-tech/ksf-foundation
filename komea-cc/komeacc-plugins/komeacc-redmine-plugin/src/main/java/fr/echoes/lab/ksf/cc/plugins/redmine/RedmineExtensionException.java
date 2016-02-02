package fr.echoes.lab.ksf.cc.plugins.redmine;

/**
 * @author dcollard
 *
 */
public class RedmineExtensionException extends Exception {

	private static final long serialVersionUID = 7979823179698756438L;

	public RedmineExtensionException(Exception e) {
		super(e);
	}

	public RedmineExtensionException(String message) {
		super(message);
	}

	public RedmineExtensionException(String message, Exception e) {
		super(message, e);
	}
}
