package fr.echoes.labs.komea.foundation.plugins.jenkins;

/**
 * @author dcollard
 *
 */
public class JenkinsExtensionException extends Exception {

	private static final long serialVersionUID = 7979823179698756438L;

	public JenkinsExtensionException(Exception e) {
		super(e);
	}

	public JenkinsExtensionException(String message) {
		super(message);
	}

	public JenkinsExtensionException(String message, Exception e) {
		super(message, e);
	}
}
